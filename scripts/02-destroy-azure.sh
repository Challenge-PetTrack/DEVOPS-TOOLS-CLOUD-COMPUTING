#!/bin/bash
set -e

echo "=========================================================="
echo "   DESTRUIÇÃO DE RECURSOS - PETTRACK AZURE CLOUD"
echo "=========================================================="

RM="563719"
RESOURCE_GROUP="rg-pettrack-${RM}"

echo "Excluindo o Resource Group: $RESOURCE_GROUP e todos os recursos vinculados..."
az group delete --name "$RESOURCE_GROUP" --yes --no-wait

echo ""
echo "✅ Solicitação de exclusão enviada com sucesso!"
echo "Os recursos da Azure serão liberados nos próximos minutos."
