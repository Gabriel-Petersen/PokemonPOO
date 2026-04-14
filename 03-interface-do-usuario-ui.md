# Interface do Usuário (UI)

Este documento explica como criar elementos visuais de interface (menus, botões, caixas de texto, imagens, etc) que aparecem **por cima** do seu jogo.

Diferente de um `GameObject` (que é parte do mundo), a UI fica **sempre visível e em primeiro plano**, e suas coordenadas não dependem da câmera.

---

## 1) O que é UI e por que é diferente?

**GameObjects** vivem no "mundo" do seu jogo. Se a câmera se move, eles se movem com ela.

**UI** é fixa na tela. Exemplo: menu, pontuação, vida do jogador. Não importa onde a câmera está, a UI continua no mesmo lugar.

---

## 2) A base: `UiElement`

Todos os elementos de UI herdam de `UiElement`.

`UiElement` oferece:

- `Transform` (posição e escala),
- Sistema de desenho (`draw()`),
- Reação ao mouse (clique, entrar, sair),
- Hierarquia (pai/filho).

Você quase nunca vai herdar de `UiElement` diretamente. A engine já oferece componentes prontos:

- `UiButton` (botão clicável),
- `UiImage` (imagem ou cor sólida),
- `UiInputText` (caixa de texto),
- `UiText` (texto simples),
- `UiProgressBar` (barra de progresso).

---

## 3) Posicionamento básico com `Transform`

Todo `UiElement` tem um `Transform` que guarda posição e escala.

### Configurar posição e tamanho

```java
var button = new UiButton("Clica aqui", () -> { /* ação */ });

button.getTransform().setPosition(100, 50);  // X=100, Y=50
button.getTransform().setScale(150, 40);    // Largura=150, Altura=40

gamePanel.addElement(button);
```

### Sistema de coordenadas

- **X** aumenta para a **direita**,
- **Y** aumenta para **baixo**,
- **(0, 0)** é o canto superior esquerdo da tela.

Exemplo com resolução 1120x630:

```
(0,0) ─── X ──→ (1120, 0)
  │
  Y
  │
  ↓
(0,630) ─────→ (1120, 630)
```

---

## 4) Âncoras: posicionamento inteligente

Às vezes você quer um botão **sempre no canto inferior direito** da tela, ou **sempre centralizado**. Para isso, existem **âncoras**.

Uma âncora define o "ponto de referência" de um elemento.

### Âncoras disponíveis

```
TOP_LEFT       TOP_CENTER      TOP_RIGHT
           ┌─────────────────┐
           │ ┌─────────────┐ │
           │ │             │ │
LEFT_CENTER┤ │   CENTER    │ ├RIGHT_CENTER
           │ │             │ │
           │ └─────────────┘ │
           └─────────────────┘
BOTTOM_LEFT  BOTTOM_CENTER  BOTTOM_RIGHT
```

### Como usar

```java
var button = new UiButton("Salvar", () -> { /* ... */ });
button.getTransform().setPosition(0, 0);
button.getUiTransform().setAnchor(UiTransform.Anchor.TOP_LEFT);
// Botão fica no canto superior esquerdo (padrão)

gamePanel.addElement(button);
```

ou

```java
var button = new UiButton("Sair", () -> { /* ... */ });
button.getTransform().setPosition(560, 315);  // centro da tela (1120/2, 630/2)
button.getUiTransform().setAnchor(UiTransform.Anchor.CENTER);
// Agora o botão **fica centrado** na tela

gamePanel.addElement(button);
```

### Por que âncoras são úteis?

Sem âncora, você coloca `(100, 50)` e o canto superior esquerdo do elemento fica ali.

Com âncora `CENTER`, o **centro** do elemento fica em `(100, 50)`. Muito mais intuitivo!

---

## 5) O arquivo `UiTransform`

`UiTransform` é uma versão especial de `Transform` para UI que **adicionou** o conceito de âncora.

Você acessa assim:

```java
button.getUiTransform().setAnchor(UiTransform.Anchor.CENTER);
```

Tudo que você fazia com `Transform` normal, você continua fazendo:

```java
button.getTransform().setPosition(100, 50);
button.getTransform().setScale(200, 60);
```

---

## 6) Componentes de UI prontos

### `UiButton` — Botão clicável

Um botão que executa uma ação quando clicado.

```java
var button = new UiButton("Começar", () -> {
    System.out.println("Jogo iniciado!");
});

button.getTransform().setPosition(460, 290);
button.getTransform().setScale(200, 50);

gamePanel.addElement(button);
```

#### O que é essa coisa depois de `()->` ?

É uma **função lambda** — uma forma compacta de escrever um bloco de código que será executado depois.

Tecnicamente, é um `Runnable`: uma "coisa que pode ser executada" sem parâmetros e sem retornar valor.

Pense assim:

- `() -> { ... }` = "quando clicarem, execute isto aqui".
- `() ->` = sem parâmetros de entrada,
- `{ ... }` = o código que roda.

Se for só uma linha, pode omitir as chaves:

```java
var button = new UiButton("Ok", () -> System.out.println("Clicou!"));
```

#### Cores do botão

Por padrão, o botão tem cor branca e fica cinza ao passar o mouse. Você pode trocar:

```java
button.setForegroundColor(Color.cyan);  // cor padrão
button.setBackgroundColor(Color.blue);  // cor ao passar mouse
```

---

### `UiImage` — Imagem ou cor sólida

Desenha uma imagem ou um retângulo com cor.

#### Com cor (quadrado colorido)

```java
var imagem = new UiImage(50, 50, Color.red);  // 50x50, vermelho
imagem.getTransform().setPosition(100, 100);
gamePanel.addElement(imagem);
```

#### Com imagem (sprite)

```java
var imagem = new UiImage("caminho/para/arquivo.png");
imagem.getTransform().setScale(100, 100);
gamePanel.addElement(imagem);
```

---

### `UiInputText` — Caixa de texto

Permite o usuário digitar e pressionar Enter para submeter.

```java
var inputBox = new UiInputText((textoDigitado) -> {
    System.out.println("Usuário digitou: " + textoDigitado);
});

inputBox.getTransform().setPosition(50, 150);
inputBox.getTransform().setScale(200, 30);

gamePanel.addElement(inputBox);
```

Aqui, `(textoDigitado) -> { ... }` é uma função que **recebe o texto** como parâmetro.

**Comportamento:**

- Clique para focar (o cursor aparece),
- Digite normalmente,
- Pressione Enter para submeter,
- O callback é executado com o texto digitado.

---

### `UiText` — Texto simples

Apenas mostra um texto na tela.

```java
var label = new UiText("Bem-vindo!");
label.setColor(Color.white);
label.setFont("Arial", 0, 24);  // fonte, estilo, tamanho
label.getTransform().setPosition(50, 50);

gamePanel.addElement(label);
```

---

## 7) Hierarquia: elementos dentro de elementos

UI geralmente é hierárquica: um "painel" contém botões, textos, etc.

Na engine, você faz isso com **pai e filho** (parent e child).

### Criando uma hierarquia

```java
// Crear o painel (pai)
var painel = new UiImage(300, 200, Color.darkGray);
painel.getTransform().setPosition(100, 100);
painel.getTransform().setScale(300, 200);
gamePanel.addElement(painel);  // Adiciona ao jogo

// Criar um botão (filho)
var button = new UiButton("Ok", () -> { /* ... */ });
button.getTransform().setPosition(20, 20);  // Relativo ao pai!
button.getTransform().setScale(100, 50);
painel.addChild(button);  // Adiciona como filho do painel, NÃO ao jogo
```

### Comportamento pai-filho

- Filhos são posicionados **relativos ao pai**,
- Se você move o pai, os filhos se movem junto,
- Se você esconde o pai, os filhos desaparecem também,
- Você só adiciona o pai ao jogo, não os filhos diretamente.

---

## 8) Exemplo prático: Menu simples

```java
// Painel de fundo
var fundoMenu = new UiImage(400, 300, Color.black);
fundoMenu.getTransform().setPosition(560, 315);
fundoMenu.getTransform().setScale(400, 300);
fundoMenu.getUiTransform().setAnchor(UiTransform.Anchor.CENTER);
gamePanel.addElement(fundoMenu);

// Título do menu
var titulo = new UiText("Menu Principal");
titulo.setColor(Color.yellow);
titulo.getTransform().setPosition(20, 20);
fundoMenu.addChild(titulo);

// Botão "Jogar"
var btnPlay = new UiButton("Jogar", () -> System.out.println("Iniciando jogo..."));
btnPlay.getTransform().setPosition(50, 80);
btnPlay.getTransform().setScale(300, 50);
fundoMenu.addChild(btnPlay);

// Botão "Sair"
var btnExit = new UiButton("Sair", () -> System.exit(0));
btnExit.getTransform().setPosition(50, 150);
btnExit.getTransform().setScale(300, 50);
fundoMenu.addChild(btnExit);
```

---

## 9) Dicas práticas

### Posicionamento responsivo

Se você quer que algo fique sempre no **canto inferior direito**, use:

```java
var button = new UiButton("Menu", () -> { /* ... */ });
button.getTransform().setPosition(1120 - 110, 630 - 60);  // 10 pixels de margem
button.getUiTransform().setAnchor(UiTransform.Anchor.BOTTOM_RIGHT);
gamePanel.addElement(button);
```

### Organizando UI em painéis

Em vez de adicionar 20 elementos diferentes direto no `gamePanel`, crie um painel e coloque tudo dentro. Facilita muito mover, esconder, etc.

### Testando interatividade

Para testar botões e inputs durante desenvolvimento:

```java
var button = new UiButton("Teste", () -> {
    System.out.println("Funcionou!");  // Isso aparece no console
});
```

---

## Resumo das classes de UI

| Classe | Uso |
|--------|-----|
| `UiButton` | Botão clicável com ação |
| `UiImage` | Imagem ou retângulo colorido |
| `UiInputText` | Caixa de texto interativa |
| `UiText` | Texto fixo |
| `UiProgressBar` | Barra de progresso |

---

## Próximos passos

Veja o exemplo `UiElements` em `examples/` para uma demonstração completa com posicionamento, âncoras e hierarquia.

Para lógica mais complexa (animações de UI, transições, eventos em cadeia), você pode combinar UI com o sistema de eventos que será documentado em outro guia.
