import { useShow } from "@refinedev/core";
import type { TenantDetail } from "../types/tenant.types";

export function useTenantDetail(id: string) {
  const { query } = useShow<TenantDetail>({
    resource: "tenants",
    id,
  });
  return {
    tenant: query.data?.data,
    isLoading: query.isLoading,
    refetch: query.refetch,
  };
}
