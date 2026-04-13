"use client";

import { useState } from "react";
import { Descriptions, Tag, Button, Table } from "antd";
import type { TenantDetail, SubscriptionHistoryItem } from "../../tenants/types/tenant.types";
import { ActivatePlanModal } from "./ActivatePlanModal";

interface Props {
  tenant: TenantDetail;
  onRefresh: () => void;
}

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

export function SubscriptionSection({ tenant, onRefresh }: Props) {
  const [modalOpen, setModalOpen] = useState(false);

  const historyColumns = [
    {
      title: "Plan",
      dataIndex: "planId",
      key: "planId",
      render: (v: string) => v,
    },
    {
      title: "Estado",
      dataIndex: "status",
      key: "status",
      render: (v: string) => <Tag color={STATUS_COLOR[v] ?? "default"}>{v}</Tag>,
    },
    {
      title: "Inicio",
      dataIndex: "startedAt",
      key: "startedAt",
      render: (v: string) => new Date(v).toLocaleDateString("es-BO"),
    },
    {
      title: "Vence",
      dataIndex: "endsAt",
      key: "endsAt",
      render: (v: string | null) =>
        v ? new Date(v).toLocaleDateString("es-BO") : "—",
    },
    {
      title: "Activado",
      dataIndex: "activatedAt",
      key: "activatedAt",
      render: (v: string) => new Date(v).toLocaleString("es-BO"),
    },
  ];

  return (
    <>
      <Descriptions
        title={
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
            <span>Suscripción</span>
            <Button type="primary" size="small" onClick={() => setModalOpen(true)}>
              Activar plan
            </Button>
          </div>
        }
        bordered
        column={2}
      >
        <Descriptions.Item label="Plan">
          {PLAN_LABELS[tenant.planName] ?? tenant.planName}
        </Descriptions.Item>
        <Descriptions.Item label="Estado">
          {tenant.subscriptionStatus ? (
            <Tag color={STATUS_COLOR[tenant.subscriptionStatus] ?? "default"}>
              {tenant.subscriptionStatus}
            </Tag>
          ) : (
            "—"
          )}
        </Descriptions.Item>
        <Descriptions.Item label="Inicio">
          {tenant.subscriptionStartedAt
            ? new Date(tenant.subscriptionStartedAt).toLocaleDateString("es-BO")
            : "—"}
        </Descriptions.Item>
        <Descriptions.Item label="Vence">
          {tenant.subscriptionEndsAt
            ? new Date(tenant.subscriptionEndsAt).toLocaleDateString("es-BO")
            : "—"}
        </Descriptions.Item>
      </Descriptions>

      {tenant.subscriptionHistory.length > 0 && (
        <div style={{ marginTop: 24 }}>
          <p style={{ fontWeight: 600, marginBottom: 8 }}>Historial de suscripciones</p>
          <Table<SubscriptionHistoryItem>
            dataSource={tenant.subscriptionHistory}
            columns={historyColumns}
            rowKey="id"
            pagination={false}
            size="small"
          />
        </div>
      )}

      <ActivatePlanModal
        tenantId={tenant.id}
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        onSuccess={onRefresh}
      />
    </>
  );
}
