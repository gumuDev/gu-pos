import { useState, useEffect, useCallback } from "react";
import type { SupportReport } from "../types/supportReport.types";

const API_URL = process.env.NEXT_PUBLIC_API_URL;

interface PagedResult {
  data: SupportReport[];
  total: number;
}

export function useSupportReportList(page: number, pageSize: number) {
  const [result, setResult] = useState<PagedResult>({ data: [], total: 0 });
  const [isLoading, setIsLoading] = useState(true);

  const fetch_ = useCallback(async () => {
    setIsLoading(true);
    try {
      const res = await fetch(
        `${API_URL}/api/v1/admin/support/reports?page=${page - 1}&size=${pageSize}`
      );
      if (!res.ok) throw new Error("fetch_failed");
      const json: SupportReport[] = await res.json();
      setResult({ data: json, total: json.length });
    } finally {
      setIsLoading(false);
    }
  }, [page, pageSize]);

  useEffect(() => { fetch_(); }, [fetch_]);

  async function markReviewed(id: string) {
    await fetch(`${API_URL}/api/v1/admin/support/reports/${id}/reviewed`, {
      method: "PATCH",
    });
    fetch_();
  }

  return { reports: result.data, total: result.total, isLoading, refetch: fetch_, markReviewed };
}
