# Mateus - Contratos base de batalha e interacao

Objetivo
- Criar contratos minimos de treinador e interacao no dominio (sem logica de batalha).

Pacotes e arquivos (crie se nao existir)
- src/domain/battle/Trainer.java
- src/domain/battle/Player.java
- src/domain/battle/NpcTrainer.java
- src/domain/battle/WildTrainer.java
- src/domain/battle/Team.java
- src/domain/battle/ActionResult.java
- src/domain/shared/Interactable.java

O que implementar (traduzido do diagrama)
- ActionResult enum: SUCCESS, FAILED, INVALID_ACTION, MISSED, FLED, CAPTURED.
- Team: lista de Pokemon, activeIndex; metodos getActiveMember, hasAvailableMember, switchActive.
- Trainer interface: getDisplayName, getTeam, isWild, selectAction(BattleContext) (pode retornar null por enquanto).
- Player/NpcTrainer/WildTrainer: dados simples (displayName, team, wild) + selectAction stub.
- Interactable: interface com onInteract(Player).

Dependencias
- Pokemon (Gustavo) para Team.
- Inventory (Pedro) apenas se quiser associar inventario ao Trainer agora (opcional).

Guia do ciclo
1. Criar ActionResult enum.
2. Implementar Team com lista de Pokemon.
3. Criar Trainer interface com assinaturas.
4. Criar Player/NpcTrainer/WildTrainer com implementacoes simples.
5. Criar Interactable em domain/shared.

Referencias
- diagrams/01-battle-core-actions.puml
- diagrams/04-exploration-encounter.puml (Interactable)
- docs/01-visao-geral-da-engine.md (contexto de arquitetura)
