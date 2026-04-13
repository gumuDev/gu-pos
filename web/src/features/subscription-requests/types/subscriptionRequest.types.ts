export interface SubscriptionRequest {
  id: string;
  tenantId: string;
  planName: string;
  transactionRef: string | null;
  receiptUrl: string | null;
  status: string;
  createdAt: string;
}
