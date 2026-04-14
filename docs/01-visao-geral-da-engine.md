# Visão Geral da Engine (Swing)

Este documento é um guia rápido para entender **como o projeto roda** e **como começar a criar coisas úteis** sem precisar mergulhar em todos os detalhes da engine.

A ideia principal: existe um painel central (`GamePanel`) que atualiza e desenha tudo em ciclo contínuo (game loop). Seus objetos entram nesse ciclo ao serem adicionados nele.

---

## 1) Fluxo geral de execução do código

### Passo a passo (do início até o jogo rodar)

1. A aplicação inicia em `Main.main(...)`.
2. O `JFrame` (janela) é criado.
3. O `GamePanel` é obtido com `GamePanel.getInstance()` e colocado dentro do frame.
4. O frame é exibido.
5. Os exemplos registrados (`Example`) executam o método `run()`.
6. Uma thread entra em loop contínuo:
   - calcula `deltaTime` (tempo entre frames),
   - chama `gamePanel.updateAll(deltaTime)`,
   - chama `gamePanel.repaint()`,
   - chama `Input.endFrame()` para limpar estados de “apertou agora” e “soltou agora”.

### Resumo mental

Pense assim:

- **setup inicial** (abrir janela e configurar),
- **registro/criação de objetos** (nos exemplos ou no seu jogo),
- **loop infinito** (atualizar lógica + redesenhar tela).

---

## 2) Papel do `GamePanel` (singleton)

`GamePanel` é o **centro da engine**.

### O que ele faz

- guarda os objetos de cena (mundo) e de UI,
- recebe os novos elementos com `addElement(...)`,
- remove com `removeElement(...)`,
- chama `setup()` e `update()` dos elementos que têm lógica,
- desenha objetos da cena e depois desenha a UI,
- mantém a câmera,
- guarda o `deltaTime` atual,
- integra os `EventScheduler`.

### Por que singleton?

A classe tem uma única instância global (`getInstance()`), então todo mundo usa o mesmo painel. Isso simplifica:

- não precisa ficar passando referência do painel por toda parte,
- toda atualização e renderização acontece no mesmo lugar,
- entrada (`Input`) e UI ficam centralizadas.

### Observação importante

Quando existe algum `EventScheduler` “resolvendo” eventos, o `GamePanel.updateAll(...)` prioriza esses eventos e **não atualiza o restante da lógica naquele momento**. Isso é útil para sequências controladas (ex.: diálogo, ataque, animação guiada).

---

## 3) Padrão `Updatable` no game loop

`Updatable` representa qualquer elemento que participa do ciclo de vida de atualização.

Ele define:

- `setup()` → roda quando o elemento entra no jogo,
- `update()` → roda a cada frame,
- `onDestroy()` → roda quando o elemento é removido.

### Como isso aparece na prática

- `GameObject` já implementa `Updatable` e `Renderable`.
- `UiElement` também participa desse ciclo.
- Quando você faz `gamePanel.addElement(obj)`, o painel decide se esse elemento precisa ser atualizado e/ou desenhado.

Em outras palavras: se o objeto está no `GamePanel`, ele pode ganhar “vida” no loop.

---

## 4) Padrão matemático de vetores (`Vec2d`, `ImmutableVec2d`, `MutableVec2d`)

A engine usa vetores 2D para posição, escala, direção e deslocamento.

### Estrutura

- `Vec2d` é a interface base (leitura de `x()` e `y()` + operações como soma, subtração e normalização).
- `ImmutableVec2d` é a versão constante (não muda depois de criada).
- `MutableVec2d` é a versão mutável (pode alterar valores com `set`, `add`, `sub`, etc.).

### Onde isso é usado

No `Transform`:

- posição (`position`) é `MutableVec2d`,
- escala (`scale`) é `MutableVec2d`,
- rotação é um `double` em graus.

Isso facilita movimentação com operações simples como:

- `translate(...)`,
- `setPosition(...)`,
- `setScale(...)`.

---

## 5) Formato básico de input

A classe principal de entrada é `Input`.

Ela guarda estado de:

- teclado pressionado (`getKey`),
- teclado apertado neste frame (`getKeyDown`),
- teclado solto neste frame (`getKeyUp`),
- mouse pressionado (`getMouseButton`),
- mouse apertado neste frame (`getMouseButtonDown`),
- mouse solto neste frame (`getMouseButtonUp`),
- posição do mouse (`getMousePos`),
- texto digitado (`getInputString`).

### Regra de ouro do input por frame

`Input.endFrame()` limpa os estados de “Down/Up” no fim de cada ciclo. Isso evita eventos repetidos indevidos.

### Eixos prontos

`Input.getAxisRaw("Horizontal")` e `Input.getAxisRaw("Vertical")` já convertem teclas comuns:

- horizontal: `A`/`←` e `D`/`→`,
- vertical: `W`/`↑` e `S`/`↓`.

Retornos:

- `-1`, `0` ou `1`.

### Relação com `UiButton` e `UiInputText`

- `UiButton` reage ao clique via listener e executa um `Runnable`.
- `UiInputText` usa `Input` para foco, texto digitado e confirmação com `Enter`.

Ou seja: os componentes de UI “consomem” os dados da classe `Input`, mas a fonte principal de entrada continua sendo o `Input`.

---

## Dica prática para começar rápido

Se você quer criar um novo comportamento, normalmente o caminho é:

1. criar um objeto (ex.: classe que herda de `GameObject` ou usar um componente pronto),
2. configurar `Transform` (posição/escala/rotação),
3. adicionar no painel com `GamePanel.getInstance().addElement(...)`,
4. colocar sua lógica em `update()`.

Com isso, você já entra no ciclo principal da engine sem precisar mexer na estrutura base.

---

## Fechamento

Esta engine foi pensada para ser simples de usar em equipe:

- um painel central,
- ciclo de atualização previsível,
- entrada unificada,
- matemática 2D direta,
- UI integrada ao mesmo fluxo.

Nos próximos documentos, vale aprofundar separadamente:

- sistema de eventos (`EventScheduler` e eventos encadeados),
- UI completa (anchors, hierarquia de `UiElement`, botões e inputs),
- animação e sprites,
- organização de cenas e arquitetura de jogo.
