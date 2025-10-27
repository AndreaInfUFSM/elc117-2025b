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

-->

<!--
nvm use v14.21.1
liascript-devserver --input README.md --port 3001 --live

-->

[![LiaScript](https://raw.githubusercontent.com/LiaScript/LiaScript/master/badges/course.svg)](https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2025b/main/classes/25/README.md)

# Projeto final de programação

Avaliação no segundo bimestre:


- Apresentação de exercícios (peso 2)
- Projeto final de programação (peso 8)
  

## Objetivos

Neste projeto, a turma vai:

1. Aplicar recursos de programação orientada a objetos no desenvolvimento de um projeto temático original e adaptado ao interesse/experiência dos estudantes
2. Aprofundar a compreensão de paradigmas de programação além do básico visto em aula
3. Desenvolver um projeto em etapas, com evidências de construção/teste incremental de código e uso de boas práticas de programação orientada a objetos
4. Experimentar um processo de disponibilização pública do projeto na web

## Requisitos

O projeto deve: 

1. Seguir o tema "gamificação", com incorporação de características de jogos para engajar, motivar ou aprimorar a experiência de usuário em um contexto que vai além do puro entretenimento

2. Utilizar um framework em Java, preferencialmente libGDX e/ou Javalin (outros são possíveis para quem já tem experiência, mediante justificativa/validação)

3. Conter pelo menos 2 classes (preferencialmente relacionadas entre si), que representem recursos de gamificação (por exemplo: challenges, points, achievements, leaderboards, etc.)



## Etapas e prazos

As etapas do trabalho são as seguintes (veja também detalhamento nas páginas a seguir):

1. Concepção da proposta de projeto, individualmente ou em dupla
2. Criação do repositório, com registro de nome de equipe (individual ou dupla) - **até 30/10**
3. Registro **individual** da proposta em um formulário próprio, para validação pela professora - **até 30/10**
4. Desenvolvimento da proposta, com commits frequentes no repositório e registros de progresso no README.md 
5. Publicação de versão inicial na web (implementação parcial) - **até 13/11**
6. Finalização do README.md e publicação/entrega final na web - **até 26/11**
7. Apresentação - dias 27/11 e 02/12


### Repositório (até 30/10)

- Criação de repositório: https://classroom.github.com/a/e3my3aOQ (primeiro integrante de cada projeto define nome da equipe, próximo integrante seleciona equipe)
- Adição da pasta oculta `.devcontainer` ao repositório, para permitir execução do projeto no Codespaces
- Prazo: até 30/10

### Proposta e validação (até 30/10)

- Enviar proposta do projeto neste formulário: https://forms.gle/Hw6NmSrDGxzDWiza9
- Validação do tema pela professora (aguarde email com possíveis ajustes - a validação será em ordem de recebimento)
- Prazo: até 30/10

### Desenvolvimento 

- Para iniciar o desenvolvimento, é recomendável antes executar os exemplos fornecidos (libGDX e/ou Javalin)

  - Com libGDX, é necessário configurar o projeto para build web/html desde o início!

- O desenvolvimento deve ser incremental, com commits no repositório a cada incremento e registros de progresso no README.md 
- Testes (unitários ou outro tipo) são sempre bem-vindos, mas ficarão como opcionais neste projeto

### Publicação inicial na web (até 13/11)

- Para projetos com libGDX, será usado o itch.io
- Para projetos com Javalin, recomendável render.com (ou similar)
- Orientações sobre isso nas próximas aulas

### Finalização: README e publicação/entrega (até 26/11)

- A publicação do projeto na web, em sua versão final, deve ser feita até 26/11, na plataforma adequada ao framework usado
- No repositório de entrega, deve constar:

  - Pasta oculta .devcontainer (conforme exemplos nos repositórios de exercícios), contendo as configurações básicas do ambiente de desenvolvimento 
  - Código-fonte do projeto (possivelmente desmembrado em mais de um projeto, se necessário)
  - README.md conforme a estrutura abaixo

- Estrutura e conteúdo do README:

  1. Identificação: nome(s) e curso
  2. Proposta: descrição do tema/objetivo do trabalho, conforme a proposta validada
  3. Processo de desenvolvimento: comentários sobre etapas do desenvolvimento, incluindo detalhes técnicos sobre os recursos de orientação a objetos utilizados, sobre erros/dificuldades/soluções e sobre as contribuições de cada integrante (não usar IA para gerar esses comentários!)
  4. Diagrama de classes: imagem com diagrama de classes do projeto
  5. Orientações para execução: instalação de dependências, etc.
  6. Resultado final: demonstrar execução em GIF animado ou vídeo curto 
  7. Referências e créditos (incluindo alguns prompts, quando aplicável)


### Apresentação

- Demonstração do projeto para a turma
- Apresentação do diagrama de classes

## Rubricas de avaliação

<!-- data-type="none" -->
| Descrição   | Nota   |
| :--------- | :--------- |
| Trabalho muito original, completamente alinhado com objetivos, requisitos e etapas de desenvolvimento, contendo muitas evidências de compreensão e aprofundamento de programação orientada a objetos, muito além dos exemplos fornecidos | 10 a 12 |
| Trabalho original, alinhado com objetivos e com a maior parte dos requisitos e etapas de desenvolvimento, contendo evidências de empenho e compreensão de programação orientada a objetos, suficientemente além dos exemplos fornecidos  | 7 a 9 |
| Trabalho alinhado com objetivos, mas com poucos requisitos e etapas satisfeitos, pouco aprofundamento, mas mesmo assim contendo alguma evidência de empenho e conhecimento de programação orientada a objetos | 5 a 7 |
| Trabalho não entregue ou com indícios de desonestidade acadêmica / geração de código sem evidências de compreensão, ou feito de última hora (sem evidências de empenho e progresso)  | 0 a 5 |

## Regras de Conduta


- É permitido consulta a qualquer material/ferramenta que contribua para o progresso neste trabalho, desde que as fontes sejam citadas **detalhadamente** no README.md. Isso inclui dar créditos a tudo que foi consultado: sites, geradores de código (incluindo exemplos de prompts), material de aula, colegas, etc.

- Deve ficar claro que a entrega tem partes de autoria própria (ideias, adaptação, integração, etc.). Cada estudante deve ter domínio do código entregue e ser capaz de responder perguntas sobre ele.

- Trechos de código aproveitados/gerados devem ser acompanhados de evidências de compreensão. Isso inclui comentários e reflexões pessoais, versões com erros, tentativas de solução, etc.

- A entrega de trabalho que não é produto do próprio esforço/aprendizado será considerado meio **fraudulento** para lograr aprovação, enquadrado como **infração disciplinar discente grave**, conforme Art. 11, inciso XVI do [Código de Ética e Convivência Discente da UFSM](https://www.ufsm.br/pro-reitorias/proplan/codigo-de-etica-e-convivencia-discente-da-universidade-federal-de-santa-maria).


