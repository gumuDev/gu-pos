"use client";

import { useState } from "react";
import { Modal, Select, Button, message } from "antd";
import { activateSubscription } from "../services/subscriptionService";

const PLAN_OPTIONS = [
  { label: "Basic", value: "basic" },
  { label: "Pro", value: "pro" },
];

interface Props {
  tenantId: string;
  open: boolean;
  onClose: () => void;
  onSuccess: () => void;
}

export function ActivatePlanModal({ tenantId, open, onClose, onSuccess }: Props) {
  const [planName, setPlanName] = useState<string | undefined>(undefined);
  const [loading, setLoading] = useState(false);

  async function handleActivate() {
    if (!planName) return;
    setLoading(true);
    try {
      await activateSubscription(tenantId, planName);
      message.success("Suscripción renovada");
      onSuccess();
      onClose();
    } catch {
      message.error("No se pudo activar la suscripción");
    } finally {
      setLoading(false);
    }
  }

  return (
    <Modal
      title="Renovar suscripción"
      open={open}
      onCancel={onClose}
      footer={[
        <Button key="cancel" onClick={onClose}>Cancelar</Button>,
        <Button
          key="activate"
          type="primary"
          loading={loading}
          disabled={!planName}
          onClick={handleActivate}
        >
          Renovar
        </Button>,
      ]}
    >
      <div style={{ marginTop: 16 }}>
        <p>Seleccioná el plan a renovar para este negocio:</p>
        <Select
          style={{ width: "100%" }}
          placeholder="Seleccionar plan"
          options={PLAN_OPTIONS}
          value={planName}
          onChange={setPlanName}
        />
      </div>
    </Modal>
  );
}
