import { useState, useEffect, useCallback } from "react";
import type { SubscriptionSummary } from "../types/subscription.types";

const API_URL = process.env.NEXT_PUBLIC_API_URL;

interface PagedResult {
  data: SubscriptionSummary[];
  total: number;
}

export function useSubscriptionList(page: number, pageSize: number) {
  const [result, setResult] = useState<PagedResult>({ data: [], total: 0 });
  const [isLoading, setIsLoading] = useState(true);

  const fetch_ = useCallback(async () => {
    setIsLoading(true);
    try {
      const res = await fetch(
        `${API_URL}/api/v1/admin/subscriptions?page=${page - 1}&size=${pageSize}`
      );
      if (!res.ok) throw new Error("fetch_failed");
      const json = await res.json();
      setResult({ data: json.data, total: json.total });
    } finally {
      setIsLoading(false);
    }
  }, [page, pageSize]);

  useEffect(() => { fetch_(); }, [fetch_]);

  return { subscriptions: result.data, total: result.total, isLoading, refetch: fetch_ };
}
