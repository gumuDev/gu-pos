import { Descriptions, Tag } from "antd";
import { FeatureFlagsDisplay } from "./FeatureFlagsDisplay";
import type { TenantDetail } from "../types/tenant.types";

interface Props {
  tenant: TenantDetail;
}

const BUSINESS_TYPE_LABELS: Record<string, string> = {
  restaurant: "Restaurante",
  pizzeria: "Pizzería",
  bakery: "Panadería",
  retail: "Retail",
  other: "Otro",
};

export function TenantDetailCard({ tenant }: Props) {
  return (
    <div style={{ display: "flex", flexDirection: "column", gap: 24 }}>
      <Descriptions title="Datos generales" bordered column={2}>
        <Descriptions.Item label="Nombre">{tenant.name}</Descriptions.Item>
        <Descriptions.Item label="Teléfono">{tenant.phone}</Descriptions.Item>
        <Descriptions.Item label="Moneda">{tenant.currency}</Descriptions.Item>
        <Descriptions.Item label="Tipo de negocio">
          {BUSINESS_TYPE_LABELS[tenant.businessType] ?? tenant.businessType}
        </Descriptions.Item>
        <Descriptions.Item label="Estado">
          <Tag color={tenant.status === "active" ? "green" : "red"}>{tenant.status}</Tag>
        </Descriptions.Item>
        <Descriptions.Item label="Registro">
          {new Date(tenant.createdAt).toLocaleString("es-BO")}
        </Descriptions.Item>
      </Descriptions>

      <Descriptions title="Sucursal" bordered column={2}>
        <Descriptions.Item label="Nombre">{tenant.branchName}</Descriptions.Item>
        <Descriptions.Item label="ID">{tenant.branchId}</Descriptions.Item>
      </Descriptions>

      <div>
        <p style={{ fontWeight: 600, marginBottom: 8 }}>Funcionalidades</p>
        <FeatureFlagsDisplay flags={tenant.featuresConfig} />
      </div>
    </div>
  );
}
