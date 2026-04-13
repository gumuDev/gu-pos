"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Table, Tag, Button, Typography, Space } from "antd";
import type { SubscriptionSummary } from "../types/subscription.types";
import { useSubscriptionList } from "../hooks/useSubscriptionList";
import { ActivatePlanModal } from "./ActivatePlanModal";

const STATUS_COLOR: Record<string, string> = {
  active: "green",
  expired: "red",
  pending: "orange",
};

const PLAN_LABELS: Record<string, string> = {
  local: "Local",
  basic: "Basic",
  pro: "Pro",
};

export function SubscriptionListView() {
  const [page, setPage] = useState(1);
  const pageSize = 20;
  const { subscriptions, total, isLoading, refetch } = useSubscriptionList(page, pageSize);

  const [activating, setActivating] = useState<SubscriptionSummary | null>(null);
  const router = useRouter();

  const columns = [
    {
      title: "Negocio",
      dataIndex: "tenantName",
      key: "tenantName",
    },
    {
      title: "Teléfono",
      dataIndex: "tenantPhone",
      key: "tenantPhone",
    },
    {
      title: "Plan",
      dataIndex: "planName",
      key: "planName",
      render: (v: string) => PLAN_LABELS[v] ?? v,
    },
    {
      title: "Estado",
      dataIndex: "subscriptionStatus",
      key: "subscriptionStatus",
      render: (v: string | null) =>
        v ? <Tag color={STATUS_COLOR[v] ?? "default"}>{v}</Tag> : <Tag>Sin suscripción</Tag>,
    },
    {
      title: "Vence",
      dataIndex: "subscriptionEndsAt",
      key: "subscriptionEndsAt",
      render: (v: string | null) =>
        v ? new Date(v).toLocaleDateString("es-BO") : "—",
    },
    {
      title: "",
      key: "actions",
      render: (_: unknown, record: SubscriptionSummary) => (
        <Space>
          <Button size="small" onClick={() => router.push(`/admin/tenants/${record.tenantId}`)}>
            Ver detalles
          </Button>
          <Button size="small" type="primary" onClick={() => setActivating(record)}>
            Renovar
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <Typography.Title level={3} style={{ marginBottom: 16 }}>
        Suscripciones
      </Typography.Title>

      <Table<SubscriptionSummary>
        dataSource={subscriptions}
        columns={columns}
        rowKey="tenantId"
        loading={isLoading}
        pagination={{
          current: page,
          pageSize,
          total,
          onChange: setPage,
          showTotal: (t) => `${t} negocios`,
        }}
      />

      {activating && (
        <ActivatePlanModal
          tenantId={activating.tenantId}
          open={true}
          onClose={() => setActivating(null)}
          onSuccess={() => { setActivating(null); refetch(); }}
        />
      )}
    </div>
  );
}
