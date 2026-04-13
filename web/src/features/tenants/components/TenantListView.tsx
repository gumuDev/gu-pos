"use client";

import { Typography } from "antd";
import { useTenantList } from "../hooks/useTenantList";
import { TenantTable } from "./TenantTable";

export function TenantListView() {
  const { tableProps } = useTenantList();
  return (
    <div style={{ padding: 24 }}>
      <Typography.Title level={3} style={{ marginBottom: 16 }}>
        Negocios registrados
      </Typography.Title>
      <TenantTable tableProps={tableProps} />
    </div>
  );
}
