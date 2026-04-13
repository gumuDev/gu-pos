import { useState, useEffect, useCallback } from "react";
import type { SubscriptionRequest } from "../types/subscriptionRequest.types";

const API_URL = process.env.NEXT_PUBLIC_API_URL;

interface PagedResult {
  data: SubscriptionRequest[];
  total: number;
}

export function useSubscriptionRequestList(page: number, pageSize: number) {
  const [result, setResult] = useState<PagedResult>({ data: [], total: 0 });
  const [isLoading, setIsLoading] = useState(true);

  const fetch_ = useCallback(async () => {
    setIsLoading(true);
    try {
      const res = await fetch(
        `${API_URL}/api/v1/admin/subscription-requests?page=${page - 1}&size=${pageSize}`
      );
      if (!res.ok) throw new Error("fetch_failed");
      const json = await res.json();
      setResult({ data: json.data, total: json.total });
    } finally {
      setIsLoading(false);
    }
  }, [page, pageSize]);

  useEffect(() => { fetch_(); }, [fetch_]);

  return { requests: result.data, total: result.total, isLoading, refetch: fetch_ };
}
