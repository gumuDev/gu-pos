export interface SupportReport {
  id: string;
  tenantId: string;
  type: "bug" | "suggestion";
  description: string;
  screenshotUrl: string | null;
  status: "pending" | "reviewed";
  createdAt: string;
}
