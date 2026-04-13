package com.pos.migration.domain.service;

import com.pos.migration.infrastructure.adapter.in.web.dto.MigrateCatalogRequest;
import com.pos.migration.infrastructure.adapter.in.web.dto.MigrateOrdersRequest;
import com.pos.migration.infrastructure.adapter.in.web.dto.MigrateStockRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class MigrationService {

    private static final Logger log = LoggerFactory.getLogger(MigrationService.class);

    private final JdbcTemplate jdbc;
    public MigrationService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    public void migrateCatalog(MigrateCatalogRequest req) {
        UUID tenantId = req.tenantId();
        log.info("Migrating catalog — tenantId={} categories={} products={}", tenantId,
                req.categories().size(), req.products().size());

        for (var c : req.categories()) {
            jdbc.update("""
                INSERT INTO categories (id, tenant_id, name, color, sort_order, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, NOW(), NOW())
                ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, color = EXCLUDED.color,
                    sort_order = EXCLUDED.sort_order, updated_at = NOW()
                """, c.id(), tenantId, c.name(), c.color(), c.sortOrder());
        }

        for (var p : req.products()) {
            jdbc.update("""
                INSERT INTO products (id, tenant_id, category_id, name, image_emoji, base_price, stock, is_active, sync_status, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'synced', NOW(), NOW())
                ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, image_emoji = EXCLUDED.image_emoji,
                    base_price = EXCLUDED.base_price, stock = EXCLUDED.stock,
                    is_active = EXCLUDED.is_active, updated_at = NOW()
                """, p.id(), tenantId, p.categoryId(), p.name(), p.imageEmoji(),
                    p.basePrice(), p.stock(), p.isActive());
        }

        for (var v : req.variants()) {
            jdbc.update("""
                INSERT INTO product_variants (id, tenant_id, product_id, name, price, stock, sort_order, is_active, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
                ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, price = EXCLUDED.price,
                    stock = EXCLUDED.stock, sort_order = EXCLUDED.sort_order,
                    is_active = EXCLUDED.is_active, updated_at = NOW()
                """, v.id(), tenantId, v.productId(), v.name(), v.price(),
                    v.stock(), v.sortOrder(), v.isActive());
        }

        for (var g : req.modifierGroups()) {
            jdbc.update("""
                INSERT INTO modifier_groups (id, tenant_id, product_id, name, required, sort_order, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())
                ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, required = EXCLUDED.required,
                    sort_order = EXCLUDED.sort_order, updated_at = NOW()
                """, g.id(), tenantId, g.productId(), g.name(), g.required(), g.sortOrder());
        }

        for (var o : req.modifierOptions()) {
            jdbc.update("""
                INSERT INTO modifier_options (id, tenant_id, group_id, name, price_delta, sort_order, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())
                ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, price_delta = EXCLUDED.price_delta,
                    sort_order = EXCLUDED.sort_order, updated_at = NOW()
                """, o.id(), tenantId, o.groupId(), o.name(), o.priceDelta(), o.sortOrder());
        }

        for (var c : req.combos()) {
            jdbc.update("""
                INSERT INTO combos (id, tenant_id, name, description, price, image_emoji, is_active, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
                ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, description = EXCLUDED.description,
                    price = EXCLUDED.price, image_emoji = EXCLUDED.image_emoji,
                    is_active = EXCLUDED.is_active, updated_at = NOW()
                """, c.id(), tenantId, c.name(), c.description(), c.price(), c.imageEmoji(), c.isActive());
        }

        for (var ci : req.comboItems()) {
            jdbc.update("""
                INSERT INTO combo_items (id, tenant_id, combo_id, product_id, product_name, quantity, created_at)
                VALUES (?, ?, ?, ?, ?, ?, NOW())
                ON CONFLICT (id) DO UPDATE SET quantity = EXCLUDED.quantity
                """, ci.id(), tenantId, ci.comboId(), ci.productId(), ci.productName(), ci.quantity());
        }

        log.info("Catalog migration complete — tenantId={}", tenantId);
    }

    @Transactional
    public void migrateOrders(MigrateOrdersRequest req) {
        UUID tenantId = req.tenantId();
        UUID branchId = req.branchId();
        log.info("Migrating orders — tenantId={} orders={}", tenantId, req.orders().size());

        for (var o : req.orders()) {
            Timestamp createdAt = Timestamp.from(Instant.ofEpochMilli(o.createdAt()));
            jdbc.update("""
                INSERT INTO orders (id, tenant_id, branch_id, session_id, cashier_name, subtotal,
                    discount_total, total, payment_method, cash_tendered, change_amount,
                    daily_sequence, status, sync_status, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'synced', ?, NOW())
                ON CONFLICT (id) DO NOTHING
                """, o.id(), tenantId, branchId, o.sessionUUID(), o.cashierName(),
                    o.subtotal(), o.discountTotal(), o.total(), o.paymentMethod(),
                    o.cashTendered(), o.changeAmount(), o.dailySequence(), o.status(), createdAt);
        }

        for (var i : req.orderItems()) {
            UUID variantId = resolveVariantId(i.variantId(), tenantId);
            String variantName = variantId != null ? i.variantName() : null;
            jdbc.update("""
                INSERT INTO order_items (id, tenant_id, order_id, product_id, product_name,
                    variant_id, variant_name, modifiers_json, unit_price, quantity, subtotal, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
                ON CONFLICT (id) DO NOTHING
                """, i.id(), tenantId, i.orderId(), i.productId(), i.productName(),
                    variantId, variantName, i.modifiersJson(),
                    i.unitPrice(), i.quantity(), i.subtotal());
        }

        log.info("Orders migration complete — tenantId={} orders={}", tenantId, req.orders().size());
    }

    private UUID resolveVariantId(UUID variantId, UUID tenantId) {
        if (variantId == null) return null;
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM product_variants WHERE id = ? AND tenant_id = ?",
                Integer.class, variantId, tenantId);
        return (count != null && count > 0) ? variantId : null;
    }

    @Transactional
    public void migrateStock(MigrateStockRequest req) {
        UUID tenantId = req.tenantId();
        log.info("Migrating stock — tenantId={}", tenantId);

        for (var ps : req.productStock()) {
            jdbc.update("""
                UPDATE products SET stock = ?, updated_at = NOW()
                WHERE id = ? AND tenant_id = ?
                """, ps.stock(), ps.productId(), tenantId);
        }

        for (var vs : req.variantStock()) {
            jdbc.update("""
                UPDATE product_variants SET stock = ?, updated_at = NOW()
                WHERE id = ? AND tenant_id = ?
                """, vs.stock(), vs.variantId(), tenantId);
        }

        log.info("Stock migration complete — tenantId={}", tenantId);
    }
}
