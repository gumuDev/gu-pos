"use client";

import { useState } from "react";
import { Table, Tag, Button, Typography, Space, Image, Tooltip } from "antd";
import type { SupportReport } from "../types/supportReport.types";
import { useSupportReportList } from "../hooks/useSupportReportList";

const STATUS_COLOR: Record<string, string> = {
  pending: "orange",
  reviewed: "green",
};

const STATUS_LABEL: Record<string, string> = {
  pending: "Pendiente",
  reviewed: "Revisado",
};

const TYPE_ICON: Record<string, string> = {
  bug: "🐞",
  suggestion: "💡",
};

const TYPE_LABEL: Record<string, string> = {
  bug: "Problema",
  suggestion: "Sugerencia",
};

export function SupportReportListView() {
  const [page, setPage] = useState(1);
  const pageSize = 20;
  const { reports, total, isLoading, markReviewed } = useSupportReportList(page, pageSize);

  const columns = [
    {
      title: "Tipo",
      dataIndex: "type",
      key: "type",
      render: (v: string) => (
        <Space>
          <span>{TYPE_ICON[v]}</span>
          <span>{TYPE_LABEL[v] ?? v}</span>
        </Space>
      ),
    },
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
      title: "Descripción",
      dataIndex: "description",
      key: "description",
      render: (v: string) => (
        <Tooltip title={v}>
          <Typography.Text ellipsis style={{ maxWidth: 300 }}>
            {v}
          </Typography.Text>
        </Tooltip>
      ),
    },
    {
      title: "Captura",
      dataIndex: "screenshotUrl",
      key: "screenshotUrl",
      render: (v: string | null) =>
        v ? (
          <Image
            src={v}
            width={48}
            height={48}
            style={{ objectFit: "cover", borderRadius: 4 }}
            preview={{ src: v }}
            alt="captura"
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
      render: (_: unknown, record: SupportReport) =>
        record.status === "pending" ? (
          <Button size="small" onClick={() => markReviewed(record.id)}>
            Marcar revisado
          </Button>
        ) : null,
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <Typography.Title level={3} style={{ marginBottom: 16 }}>
        Reportes de soporte
      </Typography.Title>

      <Table<SupportReport>
        dataSource={reports}
        columns={columns}
        rowKey="id"
        loading={isLoading}
        pagination={{
          current: page,
          pageSize,
          total,
          onChange: setPage,
          showTotal: (t) => `${t} reportes`,
        }}
      />
    </div>
  );
}
