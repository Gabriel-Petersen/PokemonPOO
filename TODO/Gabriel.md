# Gabriel — Infraestrutura de NPCs, UI de Batalha/Loja e Fila de Eventos

### Objetivo

Finalizar a camada física de movimentação e animação do jogador, implementar o ecossistema hierárquico dos NPCs utilizando herança sobre `GameObject`, e projetar as telas estáticas (HUDs) e a fila de animação visual (`EventScheduler`) que sustentarão o combate e o comércio.

### Pacotes e Arquivos (Crie ou Modifique)

* `src/game/player/Player.java` (Finalizar animação de corrida) -> OK
* `src/game/entities/Npc.java` (Classe base de NPC físico e interativo)  -> OK
* `src/game/entities/NpcSeller.java` (Especialização de comércio) 
* `src/game/entities/NpcTrainer.java` (Especialização de batalha) 
* `src/game/integration/BattleSession.java` (Esqueleto estrutural) 
* `src/game/ui/BattleHud.java` e `ShopHud.java` (Camadas visuais da engine) 

### O que Implementar

#### 1. Camada Física de Entidades (NPCs por Herança)

* **`Player`:** Integrar as sprites/frames da animação de corrida (*running*) atreladas ao input do teclado.

* **`Npc` (Base):** Criar como subclasse de `GameObject` e implementar `Interactable`. Deve conter a lógica de colisão e o trigger para disparar o método abstrato ou sobrescritível `onInteract(Player)`.

* **`NpcSeller`:** Herda de `Npc`. Carrega o catálogo de `ItemStack` do Pedro e, no `onInteract`, congela o input do mapa e invoca o `ShopHud`.

* **`NpcTrainer`:** Herda de `Npc`. Carrega os dados de `Trainer`/`Team` (do Mateus) e um estado `defeated`. No `onInteract`, se não estiver derrotado, prepara a transição de contexto para a batalha.

#### 2. Roteiro Visual de HUDs (Camada `engine.ui`)

* **`ShopHud`:** Interface gráfica de compra e venda. Deve renderizar os itens do catálogo em formato de lista e botões funcionais (`UiButton`) mapeados para disparar as checagens matemáticas do `ShopService` do Pedro.

* **`BattleHud`:** Desenhar o layout completo e estático do combate utilizando os elementos nativos da engine:

* Barras de progresso (`UiProgressBar`) posicionadas para representar o HP do Pokémon aliado e do oponente.

* Caixas de texto (`UiText`) destinadas ao log de combate e menus de seleção.

> *Nota:* Deixar os botões de ações invisíveis ou bloqueados nesta fase.


#### 3. Infraestrutura de Sincronia (`EventScheduler`)

* **`EventScheduler`:** Desenvolver a estrutura de fila de comandos visuais sequenciais. É o motor que impede o jogo de processar e cuspir os resultados do turno instantaneamente na tela.

* Deve expor métodos para enfileirar tarefas assíncronas simples (ex: "reduzir barra de vida em 20 unidades de forma suave", "exibir texto 'Pikamon usou Ember' por 1.5 segundos", "piscar sprite do Pokémon").

* O loop do jogo (`update`) consumirá essa fila elemento por elemento, liberando o input do jogador apenas quando a fila estiver vazia.

#### 4. Casca da `BattleSession`

* Instanciar o objeto de controle da sessão de batalha (`BattleSession`). Neste ciclo, ela não roda o loop de turnos automático, mas deve ser capaz de receber o contexto e inicializar o estado visual do `BattleHud` na tela.

Excelente adição. Centralizar a criação de dados dinâmicos em arquivos de texto (como `.txt`, `.csv` ou `.json`) é a melhor prática para limpar a verbosidade do código Java e isolar os dados de design da lógica do motor.

Aqui está o escopo desse extra para o seu **TODO**, integrando a infraestrutura necessária:

#### Extra: `SpeciesLoader` (Data Management)

**Objetivo:**
Implementar um gerenciador em formato **Singleton** responsável por realizar o parsing de um arquivo de configuração de texto estruturado, instanciando e armazenando todas as `Species` do jogo em memória durante a inicialização.

**O que Implementar:**

* **Padrão Singleton:** Garantir construtor privado e o método de acesso global `public static SpeciesLoader getInstance()`.
* **Coleção Indexada:** Manter um mapa interno `Map<Integer, Species> speciesRegistry` para armazenar as instâncias indexadas pelo seu `dexNumber`. Extra: criar um `Enum` de espécies com correspondência para o mapa.


* **Método `public void loadSpecies(String filePath)`:**
1. Ler o arquivo de texto linha por linha utilizando `BufferedReader` ou `Scanner`.
2. Ignorar linhas vazias ou que comecem com caracteres de comentário (ex: `#`).
3. Realizar o *split* dos dados por um delimitador padrão (ex: vírgula ou ponto e vírgula).
4. **Parsing Automático:** Converter as strings lidas para os tipos corretos da classe `Species`:
    * `Integer` para `dexNumber` e `level`.
    * `String` para `name`.
    * `ElementType` (via `ElementType.valueOf(string)`) para os tipos primário e secundário.
    * `Stats`: Mapear a sequência de inteiros para reconstruir o objeto de atributos base (`HP`, `ATTACK`, `DEFENSE`, etc.).
5. Instanciar o objeto `Species` configurado e inseri-lo no mapa interno.
* **Método de Consulta `public Species getSpecies(int dexNumber)`:** Retorna a espécie cadastrada a partir do número da Pokédex.

**Exemplo de formato sugerido para o seu arquivo de texto (`species.txt`):**

```text
# id, nome, tipo1, tipo2, hp, atk, def, spatk, spdef, speed
1,Bulbasaur,GRASS,POISON,45,49,49,65,65,45
4,Charmander,FIRE,NONE,39,52,43,60,50,65
7,Squirtle,WATER,NONE,44,48,65,50,64,43
```