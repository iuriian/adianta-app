#!/usr/bin/env python3
"""Converte os relatorios checkstyle do Detekt em markdown para o comentario do PR.

Uso: detekt-md.py <arquivo-de-saida>

Imprime no stdout o total de achados distintos, para o job decidir se comenta.
Os relatorios .md do Detekt nao servem para isso: eles sempre tem conteudo
(metricas) mesmo com zero achados, e o mesmo source set e analisado por mais de
uma task (ex. detektMainAndroid e detektAndroidMainSourceSet), o que duplicaria
cada achado.
"""

import glob
import os
import sys
import xml.etree.ElementTree as ET

LIMITE = 60000
PADRAO = "*/build/reports/detekt/*.xml"


def coleta():
    """Le todos os relatorios e devolve os achados distintos, ordenados."""
    vistos = set()
    for caminho in sorted(glob.glob(PADRAO)):
        try:
            raiz = ET.parse(caminho).getroot()
        except (ET.ParseError, OSError):
            continue
        for no in raiz.findall("file"):
            arquivo = (no.get("name") or "").replace(os.sep, "/")
            if os.path.isabs(arquivo):
                arquivo = os.path.relpath(arquivo).replace(os.sep, "/")
            for erro in no.findall("error"):
                vistos.add(
                    (
                        arquivo,
                        int(erro.get("line") or 0),
                        (erro.get("source") or "?").rsplit(".", 1)[-1],
                        " ".join((erro.get("message") or "").split()),
                    )
                )
    return sorted(vistos)


def markdown(achados):
    linhas = ["## Analise estatica (Detekt)", ""]
    if not achados:
        return linhas + ["Nenhum achado nos relatorios."]

    por_regra = {}
    for _, _, regra, _ in achados:
        por_regra[regra] = por_regra.get(regra, 0) + 1
    resumo = ", ".join(
        "`{0}` x{1}".format(regra, qtd)
        for regra, qtd in sorted(por_regra.items(), key=lambda par: (-par[1], par[0]))
    )

    linhas += ["{0} achado(s): {1}".format(len(achados), resumo), ""]
    linhas += ["| Arquivo | Linha | Regra | Mensagem |", "| --- | --- | --- | --- |"]
    barra = chr(92) + "|"
    for arquivo, linha, regra, mensagem in achados:
        linhas.append(
            "| `{0}` | {1} | `{2}` | {3} |".format(
                arquivo, linha, regra, mensagem.replace("|", barra)
            )
        )
    return linhas


def main():
    achados = coleta()
    corpo = chr(10).join(markdown(achados)) + chr(10)
    if len(corpo) > LIMITE:
        corpo = corpo[:LIMITE] + chr(10) * 2 + "Tabela truncada, baixe o artefato `detekt`."
    with open(sys.argv[1], "w", encoding="utf-8") as saida:
        saida.write(corpo)
    print(len(achados))


main()
