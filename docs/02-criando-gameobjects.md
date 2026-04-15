# Criando GameObjects

Este documento explica **como criar objetos do seu jogo** começando do zero.

Um `GameObject` é a base de tudo que você vê na tela e que pode ter comportamento. Pense como um bloco de construção: você define como ele se parece (renderização), como ele se move/age (lógica), e a engine cuida do resto.

---

## 1) O que é um GameObject?

`GameObject` é uma classe que combina:

- **Visual** (como é mostrado na tela),
- **Comportamento** (o que faz a cada frame),
- **Transformação** (posição, escala, rotação).

Exemplos: jogador, inimigo, parede, projectil, efeito visual, etc.

---

## 2) Estrutura básica: criar sua primeira classe

Aqui está o esqueleto mínimo:

```java
import engine.core.GameObject;
import engine.rendering.Renderer;

public class MeuObjeto extends GameObject {

    public MeuObjeto() {
        // Configuração inicial (opcional)
        transform.setPosition(100, 100);  // posição X, Y
        transform.setScale(2, 2);        // escala X, Y
    }

    @Override
    protected Renderer createSwingRenderer() {
        // Você DEVE implementar isto!
        // Retorna como o objeto será desenhado
    }

    @Override
    public void setup() {
        // Roda uma vez quando o objeto entra no jogo
        renderer = createSwingRenderer();
    }

    @Override
    public void update() {
        // Roda a cada frame
        // Aqui você coloca a lógica (movimento, IA, etc)
    }
}
```

Depois, usá-lo é simples:

```java
GamePanel gamePanel = GamePanel.getInstance();
gamePanel.addElement(new MeuObjeto());
```

---

## 3) Renderização: como o objeto aparece

### O método `createSwingRenderer()`

Este método é **obrigatório** e deve retornar um `Renderer`.

O `Renderer` é responsável por desenhar o objeto. Você tem algumas opções:

#### Opção 1: usar um `SpriteRenderer` (para imagens/sprites)

```java
@Override
protected Renderer createSwingRenderer() {
    return new SpriteRenderer("caminho/para/imagem.png");
}
```

`SpriteRenderer` usa imagens (PNG, JPG, etc) como visual do objeto. Se você chamar `getRenderer()` como `SpriteRenderer`, pode trocar a imagem dinamicamente:

```java
SpriteRenderer spr = (SpriteRenderer) renderer;
spr.setImage(outraImagem);
```

#### Opção 2: usar `ShapeFactory` (para formas geométricas)

Para criar formas prontas (retângulos, círculos):

```java
import engine.rendering.ShapeFactory;
import java.awt.Color;

@Override
protected Renderer createSwingRenderer() {
    return ShapeFactory.createRect(50, 30, Color.blue);
}
```

ou

```java
@Override
protected Renderer createSwingRenderer() {
    return ShapeFactory.createCircle(25, Color.red);
}
```

#### Opção 3: desenho customizado (avançado)

Se quiser desenho mais complexo, use uma lambda ou classe anônima:

```java
@Override
protected Renderer createSwingRenderer() {
    return (g2d, transform) -> {
        // g2d é o contexto de desenho (Graphics2D)
        // transform contém posição, escala, rotação
        
        g2d.setColor(Color.green);
        g2d.fillRect(0, 0, 50, 50);
    };
}
```

### O fluxo de renderização

1. Você implementa `createSwingRenderer()`,
2. Em `setup()`, você atribui a ele o renderer com `renderer = createSwingRenderer()`,
3. A engine chama `draw()` a cada frame,
4. O renderer usa `Graphics2D` para desenhar baseado no `Transform` (posição, escala, rotação).

---

## 4) Ciclo de vida: `setup()` e `update()`

### `setup()`

Roda **uma única vez** quando o objeto é adicionado ao jogo.

Ideal para:

- atribuir o renderer,
- inicializar o animator (se tiver animação),
- carregar dados,
- preparar estado inicial.

### `update()`

Roda **a cada frame**.

Ideal para:

- processar entrada (teclado, mouse),
- calcular movimento,
- atualizar animação,
- verificar colisões,
- lógica de comportamento.

### Exemplo com lógica simples

```java
@Override
public void setup() {
    renderer = createSwingRenderer();
    velocidade = 5;
}

@Override
public void update() {
    // Move para a direita a cada frame
    transform.translate(velocidade, 0);
}
```

---

## 5) Movimento com `Transform`

O `Transform` guarda **posição**, **escala** e **rotação**.

### Métodos principais

**Definir valores absolutos:**

```java
transform.setPosition(x, y);           // posição exata
transform.setScale(width, height);    // tamanho exato
transform.rotation = 45;              // rotação em graus
```

**Movimento relativo (translação):**

```java
transform.translate(10, 5);  // move +10 em X, +5 em Y

// ou usando vetores:
Vec2d velocity = new MutableVec2d(3, 4);
transform.translate(velocity);
```

**Distância:**

```java
double dist = transform.distanceTo(outroTransform);
```

### Exemplo: movimento com teclado

```java
@Override
public void update() {
    int horizontal = Input.getAxisRaw("Horizontal");  // -1, 0, 1
    int vertical = Input.getAxisRaw("Vertical");      // -1, 0, 1
    
    float speed = 2.5f;
    transform.translate(horizontal * speed, vertical * speed);
}
```

---

## 6) Animação básica com `Animator`

Animação é uma sequência de imagens que mudam rapidinho, criando movimento.

### Setup do Animator

```java
import engine.animation.Animator;

private Animator animator;

@Override
public void setup() {
    renderer = createSwingRenderer();
    animator = new Animator((SpriteRenderer) renderer);
    
    // Adicionar animações
    animator.addAnimation("idle", 6, true, "pasta/com/imagens");
    animator.addAnimation("walk", 8, true, "pasta/walk");
}
```

**Parâmetros:**

- nome da animação (`"idle"`),
- FPS (velocidade, ex: `6` quadros por segundo),
- loop (`true` = repete, `false` = toca uma vez),
- pasta ou lista de imagens.

### Reproduzindo animações

```java
@Override
public void update() {
    animator.update();  // IMPORTANTE: chamá-lo em update()
    
    if (estouMovendo) {
        animator.play("walk");
    } else {
        animator.play("idle");
    }
}
```

### Exemplo completo: Sprite com idle e walk

```java
public class SimplePatrol extends GameObject {
    private Animator animator;
    private int direction = 1;

    @Override
    protected Renderer createSwingRenderer() {
        return new SpriteRenderer("char/idle.png");
    }

    @Override
    public void setup() {
        renderer = createSwingRenderer();
        animator = new Animator((SpriteRenderer) renderer);
        
        animator.addAnimation("idle", 4, true, "char/idle");
        animator.addAnimation("walk", 8, true, "char/walk");
    }

    @Override
    public void update() {
        animator.update();

        // Movimento
        transform.translate(direction * 2, 0);
        if (transform.getPosition().x() > 300) direction = -1;
        if (transform.getPosition().x() < 50) direction = 1;

        // Animação
        animator.play(direction > 0 ? "walk" : "idle");
    }
}
```

---

## 7) Estados simples (máquina de estados básica)

Muitos objetos precisam alternar entre estados. Exemplo: `IDLE`, `WALKING`, `JUMPING`.

### Implementação simples com enum

```java
public class Player extends GameObject {
    
    public enum State { IDLE, WALKING, JUMPING }
    private State currentState = State.IDLE;
    private Animator animator;

    @Override
    public void update() {
        animator.update();

        // Lógica de mudança de estado
        int horizontal = Input.getAxisRaw("Horizontal");
        
        if (horizontal != 0) {
            currentState = State.WALKING;
        } else {
            currentState = State.IDLE;
        }

        // Executar lógica baseada no estado
        switch (currentState) {
            case IDLE:
                animator.play("idle");
                break;
            case WALKING:
                transform.translate(horizontal * 3, 0);
                animator.play("walk");
                break;
            case JUMPING:
                // lógica de pulo
                animator.play("jump");
                break;
        }
    }
}
```

---

## Apêndice A: Objetos sem lógica (apenas visuais)

Às vezes, você quer um objeto que **só aparece** na tela sem fazer nada (ex.: decoração, parede estática).

Você pode deixar `setup()` e `update()` vazios:

```java
public class Decoracao extends GameObject {

    @Override
    protected Renderer createSwingRenderer() {
        return new SpriteRenderer("decoracao.png");
    }

    @Override
    public void setup() {
        renderer = createSwingRenderer();
    }

    @Override
    public void update() {
        // Vazio: nada acontece
    }
}
```

Como a `update()` não faz nada, o objeto fica parado. Isso é perfeitamente válido.

---

## Apêndice B: Objetos invisíveis ou sem renderização

Se você quiser um objeto que tenha **lógica** mas **não apareça visualmente** (ex.: detector invisível de colisão, gerenciador), deixe `createSwingRenderer()` retornar `null`:

```java
public class DetoctorInvisivel extends GameObject {

    @Override
    protected Renderer createSwingRenderer() {
        return null;  // Sem visual
    }

    @Override
    public void setup() {
        // Sem renderer
    }

    @Override
    public void update() {
        // Lógica acontece normalmente
        // Ex.: verificar se algo entrou na área
    }
}
```

Objetos deste tipo **não aparecem**, mas sua lógica em `update()` continua rodando normalmente.

---

## Resumo prático

Para criar um novo `GameObject`:

1. Herde de `GameObject`,
2. Implemente `createSwingRenderer()` (visual),
3. Implemente `setup()` (inicialização),
4. Implemente `update()` (lógica a cada frame),
5. Crie uma instância e adicione com `gamePanel.addElement(novo_objeto)`.

Quanto mais prática você tiver criando GameObjects, mais natural fica pensar em "como quebrar meu jogo em pequenas partes que herdam de GameObject".

Veja o exemplo `GameObjectExamples` em `examples.GameObjectExamples` para uma implementação didática completa.
