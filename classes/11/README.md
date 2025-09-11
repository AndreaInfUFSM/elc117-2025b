<!--
author:   Andrea Charão

email:    andrea@inf.ufsm.br

version:  0.0.1

language: PT-BR

narrator: Brazilian Portuguese Female

comment:  Material de apoio para a disciplina
          ELC117 - Paradigmas de Programação
          da Universidade Federal de Santa Maria

translation: English  translations/English.md

link:     https://cdn.jsdelivr.net/chartist.js/latest/chartist.min.css

script:   https://cdn.jsdelivr.net/chartist.js/latest/chartist.min.js

-->

<!--
nvm use v14.21.1
liascript-devserver --input README.md --port 3001 --live

-->

[![LiaScript](https://raw.githubusercontent.com/LiaScript/LiaScript/master/badges/course.svg)](https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2025b/main/classes/11/README.md)

# Produção individual personalizada

Avaliação no primeiro bimestre:


- Prova de leitura/escrita de código (peso 5): 25/09
- Apresentação de exercícios (peso 2)
- Produção individual personalizada (peso 3): 28/09
  
  ⚠️ Prazo: 28/09


## Objetivos e requisitos 


1. Aprofundamento em programação funcional, com aplicação e compreensão de novos recursos
2. Personalização do tema, adaptado a interesse/experiência de cada estudante
3. Teste unitário de funções 
4. Evidências de progresso individual e construção/teste incremental do código
5. Realização em etapas


## Tema: Backend Web com Scotty


- Tema: desenvolver um serviço web que responda a requisições get/post usando Scotty, com ou sem banco de dados
- Conforme visto em aula, Scotty é uma biblioteca em Haskell para construção de backend web (inclui seu próprio servidor HTTP)

  - Ver mais sobre isso [nesta aula](https://liascript.github.io/course/?https://raw.githubusercontent.com/elc117/demo-scotty-codespace-2025b/main/README.md)

- Testes unitários:

  - A lógica do seu serviço deve ficar em funções que deverão ser testadas independentemente do Scotty
  - Você pode usar HUnit, ou pode simplesmente fazer um programa simples que chame as funções para testá-las, sem criação do servidor web

- Possibilidades de personalização:

  - Somente leitura (get): conversões, sorteios, buscas, quizzes, etc.
  - Leitura / escrita (get/post/put/delete, com persistência em banco de dados): leaderboard para game, log para quizzes/games, listas de tarefas, etc.

- Opcionais sempre bem-vindos: desde que atenda aos objetivos e requisitos do trabalho, você pode adicionar quaisquer opcionais que desejar (frontend, deploy, comparações com outras linguagens, configurações avançadas de projeto, etc.).

### Outros

- Caso você se interesse por um aprofundamento maior e tenha alguma ideia que não se encaixe em um backend web, você pode propor outro tema à professora (sujeito a validação)
- Tenha em mente que os objetivos e requisitos devem se manter
- Com uma boa justificativa, é possível propor um tema em outra linguagem de programação funcional


## Etapas

As etapas do trabalho são as seguintes (veja também detalhamento nas páginas seguintes):

1. Proposta de personalização do tema em um formulário próprio, com validação pela professora 
2. Criação e atualização do repositório
3. Desenvolvimento da proposta (estudo/pesquisa e construção/teste incremental do código), com commits frequentes no repositório
4. Elaboração de README.md e entrega final


### Proposta e validação da personalização

- Proposta de personalização do tema neste formulário: https://forms.gle/hX38pPwsBjy5rjdN7
- Validação do tema pela professora (aguarde email)

### Repositório

- Criação de repositório: https://classroom.github.com/a/7NMOLXjY
- Se você vai desenvolver no Codespaces, adicione ao repositório a pasta oculta `.devcontainer` (contida nos repositórios de exercícios), contendo as configurações básicas do ambiente de desenvolvimento Haskell

### Desenvolvimento e testes

- Desenvolvimento da proposta: estudo/pesquisa e construção/teste incremental do código, com commits frequentes no repositório
- Lembre que é importante a criação de código de teste das funções que implementam a lógica do seu serviço

### Entrega final com README

- A entrega final deve conter pelo menos 2 arquivos .hs: código principal e código de testes
- Se você vai desenvolver no Codespaces, adicione ao repositório a pasta oculta .devcontainer (contida nos repositórios de exercícios), contendo as configurações básicas do ambiente de desenvolvimento Haskell
- Estrutura e conteúdo do README:

  1. Identificação: nome e curso
  2. Tema/objetivo: descrição do tema/objetivo do trabalho, conforme a proposta validada
  3. Processo de desenvolvimento: comentários pessoais sobre o desenvolvimento, com evidências de compreensão, incluindo versões com erros e tentativas de solução
  4. Orientações para execução: instalação de dependências, etc.
  5. Resultado final: demonstrar execução em GIF animado ou vídeo curto (máximo 60s)
  6. Referências e créditos (incluindo alguns prompts, se aplicável)





## Rubricas de avaliação

<!-- data-type="none" -->
| Descrição   | Nota   |
| :--------- | :--------- |
| Trabalho muito original e personalizado, completamente alinhado com objetivos, requisitos e etapas de desenvolvimento, contendo muitas evidências de compreensão e aprofundamento de programação funcional e declarativa, muito além do que foi visto em aula | 10 a 12 |
| Trabalho original, alinhado com objetivos e com a maior parte dos requisitos e etapas de desenvolvimento, contendo evidências de empenho e compreensão de programação funcional e declarativa, suficientemente além do que foi visto em aula  | 7 a 9 |
| Trabalho alinhado com objetivos, mas com poucos requisitos e etapas satisfeitos, pouco aprofundamento, mas mesmo assim contendo alguma evidência de empenho e conhecimento de programação funcional/declarativa | 5 a 7 |
| Trabalho não entregue ou com indícios de desonestidade acadêmica / geração de código sem evidências de compreensão, ou feito de última hora (sem evidências de empenho e progresso)  | 0 a 5 |

## Regras de Conduta


- É permitido consulta a qualquer material/ferramenta que contribua para seu progresso neste trabalho, desde que você cite as fontes **detalhadamente** no README.md. Isso inclui dar créditos a tudo que consultou: sites, geradores de código (incluindo prompts), material de aula, colegas, etc.

- Deve ficar claro que a entrega tem partes de sua própria autoria (ideias, adaptação, integração, etc.). Você deve ter domínio do código entregue e ser capaz de responder perguntas sobre ele.

- Trechos de código aproveitados/gerados devem ser acompanhados de evidências de compreensão. Isso inclui comentários e reflexões pessoais, versões com erros, tentativas de solução, etc.

- A entrega de trabalho que não é produto de seu próprio esforço/aprendizado será considerado meio **fraudulento** para lograr aprovação, enquadrado como **infração disciplinar discente grave**, conforme Art. 11, inciso XVI do [Código de Ética e Convivência Discente da UFSM](https://www.ufsm.br/pro-reitorias/proplan/codigo-de-etica-e-convivencia-discente-da-universidade-federal-de-santa-maria).


