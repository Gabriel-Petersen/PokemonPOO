# Gabriel - Integracao e UI de pausa

Objetivo
- Integrar UI de pausa com metadados do player e NPCs basicos, usando apenas engine.ui (sem Swing direto).

Pacotes e arquivos (crie se nao existir)
- src/domain/player/PlayerMetadata.java
- src/game/ui/PauseMenu.java
- src/game/ui/PauseTab.java
- src/game/ui/PlayerMetadataTab.java
- src/game/Npc.java (ou src/game/integration/Npc.java)
- src/game/Player.java (ajustes)
- src/execs/Main.java (ajustes)

O que implementar (traduzido do diagrama)
- PlayerMetadata: somente nome e dinheiro (getters simples).
- PauseMenu + PauseTab: UI navegavel com abas; apenas a aba de metadados precisa mostrar dados.
- PlayerMetadataTab: mostra nome e dinheiro com UiText.
- Npc: GameObject com onInteract(Player) exibindo UI "loja WIP" via UiText ou UiImage (nunca terminal).
- Player: guardar PlayerMetadata e toggle de pausa no input.

Dependencias
- Interactable e NpcTrainer (Mateus).
- NpcSeller (Pedro) se for diferenciar NPC vendedor.

Guia do ciclo
1. Criar PlayerMetadata com nome e dinheiro.
2. Criar PauseMenu e PauseTab seguindo docs de UI.
3. Criar PlayerMetadataTab mostrando dados reais.
4. Implementar Npc com onInteract exibindo UI "loja WIP".
5. Ajustar Player para abrir/fechar pausa.
6. Ajustar Main para instanciar Player e 1 NPC de teste.

Referencias
- diagrams/05-domain-engine-integration.puml
- docs/03-interface-do-usuario-ui.md
- src/examples/UiElementExamples.java
- src/game/Player.java
- src/execs/Main.java
