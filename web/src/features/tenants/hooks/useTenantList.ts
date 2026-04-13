import { useTable } from "@refinedev/antd";
import type { TenantSummary } from "../types/tenant.types";

export function useTenantList() {
  const { tableProps } = useTable<TenantSummary>({
    resource: "tenants",
    pagination: { pageSize: 20 },
    syncWithLocation: true,
  });
  return { tableProps };
}
