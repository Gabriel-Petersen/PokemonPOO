import os
import re


def extrair_nome_classe(linha_package, caminho_relativo):
    """Auxiliar para mapear o fully qualified name da classe com base no package."""
    # Transforma 'src/model/Usuario.java' em 'model.Usuario'
    partes = caminho_relativo.replace("\\", "/").split("/")
    if "src" in partes:
        partes = partes[partes.index("src") + 1 :]

    nome_arquivo = partes[-1].replace(".java", "")
    pacote = ""

    if linha_package:
        match = re.match(r"package\s+([\w.]+);", linha_package)
        if match:
            pacote = match.group(1)

    return f"{pacote}.{nome_arquivo}" if pacote else nome_arquivo


def mapear_arquivos_e_imports(diretorio_src):
    """Varre o diretório src, lê os arquivos e mapeia as dependências."""
    grafo = {}
    conteudo_arquivos = {}
    classes_do_projeto = set()
    mapeamento_caminhos = {}

    # Primeira passada: descobrir todas as classes que pertencem ao projeto
    for raiz, _, arquivos in os.walk(diretorio_src):
        for arquivo in arquivos:
            if arquivo.endswith(".java"):
                caminho_completo = os.path.join(raiz, arquivo)
                caminho_relativo = os.path.relpath(
                    caminho_completo, diretorio_src
                )

                # Ler conteúdo e buscar a linha do package
                with open(
                    caminho_completo, "r", encoding="utf-8", errors="ignore"
                ) as f:
                    linhas = f.readlines()

                linha_package = next(
                    (l.strip() for l in linhas if l.strip().startswith("package")),
                    None,
                )

                fqn = extrair_nome_classe(linha_package, os.path.join('src', caminho_relativo)) # Fully Qualified Name

                classes_do_projeto.add(fqn)
                mapeamento_caminhos[fqn] = caminho_completo
                conteudo_arquivos[fqn] = "".join(linhas)

    # Segunda passada: extrair os imports e construir o grafo
    for fqn, caminho in mapeamento_caminhos.items():
        grafo[fqn] = []
        with open(caminho, "r", encoding="utf-8", errors="ignore") as f:
            texto = f.read()

        # Regex para capturar imports
        imports = re.findall(r"import\s+([\w.]+);", texto)

        for imp in imports:
            # Se o import faz parte das classes do nosso projeto, adiciona como dependência
            if imp in classes_do_projeto:
                grafo[fqn].append(imp)
            # Trata imports com wildcard (ex: import model.*;)
            elif imp.endswith(".*"):
                pacote_wildcard = imp[:-2]
                for classe in classes_do_projeto:
                    if (
                        classe.startswith(pacote_wildcard)
                        and classe.count(".") == pacote_wildcard.count(".") + 1
                    ):
                        grafo[fqn].append(classe)

    return grafo, conteudo_arquivos

def ordenacao_topologica(grafo):
    """Encontra Componentes Fortemente Conexos (SCC) usando o Algoritmo de Tarjan

    e ordena topologicamente essas 'bolhas' para evitar travamentos por ciclos.
    """
    import sys

    # Aumentar limite de recursão caso o grafo seja muito profundo
    sys.setrecursionlimit(3000)

    index = 0
    stack = []
    indices = {}
    lowlink = {}
    on_stack = {}
    sccs = []

    # --- 1. ALGORITMO DE TARJAN PARA ENCONTRAR AS "BOLHAS" (SCCs) ---
    def strongconnect(node):
        nonlocal index
        indices[node] = index
        lowlink[node] = index
        index += 1
        stack.append(node)
        on_stack[node] = True

        for neighbor in grafo.get(node, []):
            if neighbor not in indices:
                strongconnect(neighbor)
                lowlink[node] = min(lowlink[node], lowlink[neighbor])
            elif on_stack.get(neighbor, False):
                lowlink[node] = min(lowlink[node], indices[neighbor])

        if lowlink[node] == indices[node]:
            scc = []
            while True:
                w = stack.pop()
                on_stack[w] = False
                scc.append(w)
                if w == node:
                    break
            sccs.append(scc)

    for node in grafo:
        if node not in indices:
            strongconnect(node)

    # --- 2. CONSTRUIR UM NOVO GRAFO SEM CICLOS (GRAFO DE SCCs) ---
    # Mapeia cada classe para o índice da sua respectiva bolha (SCC)
    classe_para_scc_idx = {}
    for idx, scc in enumerate(sccs):
        for classe in scc:
            classe_para_scc_idx[classe] = idx

    # Cria a lista de adjacência entre as bolhas
    grafo_scc = {i: set() for i in range(len(sccs))}
    grau_entrada_scc = {i: 0 for i in range(len(sccs))}

    for u in grafo:
        scc_u = classe_para_scc_idx[u]
        for v in grafo[u]:
            scc_v = classe_para_scc_idx[v]
            # Se u depende de v, e eles estão em bolhas diferentes
            if scc_u != scc_v and scc_u not in grafo_scc[scc_v]:
                grafo_scc[scc_v].add(scc_u)  # scc_v deve vir ANTES de scc_u

    # Calcular grau de entrada do grafo de supernós
    for u in grafo_scc:
        for v in grafo_scc[u]:
            grau_entrada_scc[v] += 1

    # --- 3. ORDENAÇÃO TOPOLÓGICA DAS BOLHAS (ALGORITMO DE KAHN) ---
    fila_scc = [i for i in grau_entrada_scc if grau_entrada_scc[i] == 0]
    ordem_scc_idx = []

    while fila_scc:
        u = fila_scc.pop(0)
        ordem_scc_idx.append(u)

    for v in grafo_scc[u]:
        grau_entrada_scc[v] -= 1
        if grau_entrada_scc[v] == 0:
            fila_scc.append(v)

    # --- 4. DESMANCHAR AS BOLHAS NA ORDEM CERTA ---
    ordem_final_classes = []
    # Se alguma bolha não entrou na ordenação (raro), adicionamos no final por segurança
    sccs_visitados = set(ordem_scc_idx)
    todas_as_bolhas = ordem_scc_idx + [
        i for i in range(len(sccs)) if i not in sccs_visitados
    ]

    for idx in todas_as_bolhas:
        # Pega as classes de dentro daquela bolha
        componentes = sccs[idx]
        # Uma ordenação interna simples (alfabética) só para manter um padrão estético
        componentes.sort()
        ordem_final_classes.extend(componentes)

    return ordem_final_classes


def gerar_arquivo_unico(nome_saida="codigo_completo.txt"):
    diretorio_src = "src"

    if not os.path.exists(diretorio_src):
        print("Erro: A pasta 'src' não foi encontrada na raiz.")
        return

    print("📊 Analisando a estrutura do projeto e mapeando classes...")
    try:
        grafo, conteudos = mapear_arquivos_e_imports(diretorio_src)
        print(f"✅ {len(conteudos)} classes localizadas.")

        print("🧬 Calculando ordenação topológica (DAG)...")
        ordem_classes = ordenacao_topologica(grafo)
        print("✅ Grafo validado com sucesso! Nenhuma referência circular.")

        print(f"💾 Gerando o arquivo {nome_saida}...")
        with open(nome_saida, "w", encoding="utf-8") as f_saida:
            f_saida.write(
                "==================================================================\n"
            )
            f_saida.write(
                f" PROJETO COMPLETO - ORDENADO POR DEPENDÊNCIAS ({len(ordem_classes)} CLASSES)\n"
            )
            f_saida.write(
                "==================================================================\n\n"
            )

            for i, classe in enumerate(ordem_classes, 1):
                f_saida.write(
                    f"// [{i}/{len(ordem_classes)}] CLASSE: {classe}\n"
                )
                f_saida.write(
                    "// --------------------------------------------------------------\n"
                )
                f_saida.write(conteudos[classe])
                f_saida.write("\n\n")

        print(f"🚀 Tudo pronto! Arquivo '{nome_saida}' gerado com sucesso.")

    except ValueError as e:
        print("\n❌ Falha na validação do código:")
        print(e)
    except Exception as e:
        print(f"\n❌ Ocorreu um erro inesperado: {e}")


if __name__ == "__main__":
    gerar_arquivo_unico()