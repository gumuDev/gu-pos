import { TenantDetailView } from "@/features/tenants/components/TenantDetailView";

interface Props {
  params: Promise<{ id: string }>;
}

export default async function TenantDetailPage({ params }: Props) {
  const { id } = await params;
  return <TenantDetailView id={id} />;
}
