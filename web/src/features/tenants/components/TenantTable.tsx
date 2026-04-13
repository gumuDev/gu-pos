import { Table } from "antd";
import Link from "next/link";
import type { TableProps } from "antd";
import type { TenantSummary } from "../types/tenant.types";

interface Props {
  tableProps: TableProps<TenantSummary>;
}

export function TenantTable({ tableProps }: Props) {
  return (
    <Table {...tableProps} rowKey="id">
      <Table.Column title="Negocio" dataIndex="name" />
      <Table.Column title="Teléfono" dataIndex="phone" />
      <Table.Column title="Plan" dataIndex="planName" />
      <Table.Column
        title="Fecha de registro"
        dataIndex="createdAt"
        render={(value: string) => new Date(value).toLocaleDateString("es-BO")}
      />
      <Table.Column
        title=""
        render={(_: unknown, record: TenantSummary) => (
          <Link href={`/admin/tenants/${record.id}`}>Ver detalle</Link>
        )}
      />
    </Table>
  );
}
