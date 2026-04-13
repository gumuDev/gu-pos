"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Table, Tag, Button, Typography, Space, Image } from "antd";
import type { SubscriptionRequest } from "../types/subscriptionRequest.types";
import { useSubscriptionRequestList } from "../hooks/useSubscriptionRequestList";

const STATUS_COLOR: Record<string, string> = {
  pending: "orange",
  approved: "green",
  rejected: "red",
};

const STATUS_LABEL: Record<string, string> = {
  pending: "Pendiente",
  approved: "Aprobada",
  rejected: "Rechazada",
};

const PLAN_LABELS: Record<string, string> = {
  local: "Local",
  basic: "Basic",
  pro: "Pro",
};

export function SubscriptionRequestListView() {
  const [page, setPage] = useState(1);
  const pageSize = 20;
  const router = useRouter();
  const { requests, total, isLoading } = useSubscriptionRequestList(page, pageSize);

  const columns = [
    {
      title: "Tenant ID",
      dataIndex: "tenantId",
      key: "tenantId",
      render: (v: string) => (
        <Typography.Text code style={{ fontSize: 12 }}>
          {v.slice(0, 8)}…
        </Typography.Text>
      ),
    },
    {
      title: "Plan solicitado",
      dataIndex: "planName",
      key: "planName",
      render: (v: string) => PLAN_LABELS[v] ?? v,
    },
    {
      title: "Ref. transacción",
      dataIndex: "transactionRef",
      key: "transactionRef",
      render: (v: string | null) => v ?? "—",
    },
    {
      title: "Comprobante",
      dataIndex: "receiptUrl",
      key: "receiptUrl",
      render: (v: string | null) =>
        v ? (
          <Image
            src={v}
            width={48}
            height={48}
            style={{ objectFit: "cover", borderRadius: 4 }}
            preview={{ src: v }}
            alt="comprobante"
          />
        ) : (
          "—"
        ),
    },
    {
      title: "Estado",
      dataIndex: "status",
      key: "status",
      render: (v: string) => (
        <Tag color={STATUS_COLOR[v] ?? "default"}>{STATUS_LABEL[v] ?? v}</Tag>
      ),
    },
    {
      title: "Fecha",
      dataIndex: "createdAt",
      key: "createdAt",
      render: (v: string) => new Date(v).toLocaleDateString("es-BO"),
    },
    {
      title: "",
      key: "actions",
      render: (_: unknown, record: SubscriptionRequest) => (
        <Space>
          <Button
            size="small"
            onClick={() => router.push(`/admin/tenants/${record.tenantId}`)}
          >
            Ver negocio
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <Typography.Title level={3} style={{ marginBottom: 16 }}>
        Solicitudes de suscripción
      </Typography.Title>

      <Table<SubscriptionRequest>
        dataSource={requests}
        columns={columns}
        rowKey="id"
        loading={isLoading}
        pagination={{
          current: page,
          pageSize,
          total,
          onChange: setPage,
          showTotal: (t) => `${t} solicitudes`,
        }}
      />
    </div>
  );
}
