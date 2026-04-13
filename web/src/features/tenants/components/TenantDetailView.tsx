"use client";

import { Spin, Typography, Button } from "antd";
import Link from "next/link";
import { useTenantDetail } from "../hooks/useTenantDetail";
import { TenantDetailCard } from "./TenantDetailCard";

interface Props {
  id: string;
}

export function TenantDetailView({ id }: Props) {
  const { tenant, isLoading } = useTenantDetail(id);

  if (isLoading) {
    return (
      <div style={{ padding: 24, display: "flex", justifyContent: "center" }}>
        <Spin size="large" />
      </div>
    );
  }

  if (!tenant) return null;

  return (
    <div style={{ padding: 24 }}>
      <div style={{ marginBottom: 16, display: "flex", alignItems: "center", gap: 12 }}>
        <Link href="/admin/tenants">
          <Button>← Volver</Button>
        </Link>
        <Typography.Title level={3} style={{ margin: 0 }}>
          {tenant.name}
        </Typography.Title>
      </div>
      <TenantDetailCard tenant={tenant} />
    </div>
  );
}
