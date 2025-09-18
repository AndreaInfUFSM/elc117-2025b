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

@onload
window.CodeRunner = {
    ws: undefined,
    handler: {},
    connected: false,
    error: "",
    url: "",
    firstConnection: true,

    init(url, step = 0) {
        this.url = url
        if (step  >= 10) {
           console.warn("could not establish connection")
           this.error = "could not establish connection to => " + url
           return
        }

        this.ws = new WebSocket(url);

        const self = this
        
        const connectionTimeout = setTimeout(() => {
          self.ws.close();
          console.log("WebSocket connection timed out");
        }, 5000);
        
        
        this.ws.onopen = function () {
            clearTimeout(connectionTimeout);
            self.log("connections established");

            self.connected = true
            
            setInterval(function() {
                self.ws.send("ping")
            }, 15000);
        }
        this.ws.onmessage = function (e) {
            // e.data contains received string.

            let data
            try {
                data = JSON.parse(e.data)
            } catch (e) {
                self.warn("received message could not be handled =>", e.data)
            }
            if (data) {
                self.handler[data.uid](data)
            }
        }
        this.ws.onclose = function () {
            clearTimeout(connectionTimeout);
            self.connected = false
            self.warn("connection closed ... reconnecting")

            setTimeout(function(){
                console.warn("....", step+1)
                self.init(url, step+1)
            }, 1000)
        }
        this.ws.onerror = function (e) {
            clearTimeout(connectionTimeout);
            self.warn("an error has occurred")
        }
    },
    log(...args) {
        window.console.log("CodeRunner:", ...args)
    },
    warn(...args) {
        window.console.warn("CodeRunner:", ...args)
    },
    handle(uid, callback) {
        this.handler[uid] = callback
    },
    send(uid, message, sender=null, restart=false) {
        const self = this
        if (this.connected) {
          message.uid = uid
          this.ws.send(JSON.stringify(message))
        } else if (this.error) {

          if(restart) {
            sender.lia("LIA: terminal")
            this.error = ""
            this.init(this.url)
            setTimeout(function() {
              self.send(uid, message, sender, false)
            }, 2000)

          } else {
            //sender.lia("LIA: wait")
            setTimeout(() => {
              sender.lia(" " + this.error)
              sender.lia(" Maybe reloading fixes the problem ...")
              sender.lia("LIA: stop")
            }, 800)
          }
        } else {
          setTimeout(function() {
            self.send(uid, message, sender, false)
          }, 2000)
          
          if (sender) {
            
            sender.lia("LIA: terminal")
            if (this.firstConnection) {
              this.firstConnection = false
              setTimeout(() => { 
                sender.log("stream", "", [" Waking up execution server ...\n", "This may take up to 30 seconds ...\n", "Please be patient ...\n"])
              }, 100)
            } else {
              sender.log("stream", "", ".")
            }
            sender.lia("LIA: terminal")
          }
        }
    }
}

//window.CodeRunner.init("wss://coderunner.informatik.tu-freiberg.de/")
//window.CodeRunner.init("ws://localhost:4000/")
window.CodeRunner.init("wss://ancient-hollows-41316.herokuapp.com/")
@end

@LIA.c:                 @LIA.eval(`["main.c"]`, `gcc -Wall main.c -o a.out`, `./a.out`)
@LIA.prolog:            @LIA.eval(`["main.pl"]`, `none`, `swipl -s main.pl -g @0 -t halt`)
@LIA.prolog_withShell:  @LIA.eval(`["main.pl"]`, `none`, `swipl -s main.pl`)


@LIA.eval:  @LIA.eval_(false,`@0`,@1,@2,@3)

@LIA.evalWithDebug: @LIA.eval_(true,`@0`,@1,@2,@3)

@LIA.eval_
<script>
function random(len=16) {
    let chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let str = '';
    for (let i = 0; i < len; i++) {
        str += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return str;
}



const uid = random()
var order = @1
var files = []

var pattern = "@4".trim()

if (pattern.startsWith("\`")){
  pattern = pattern.slice(1,-1)
} else if (pattern.length === 2 && pattern[0] === "@") {
  pattern = null
}

if (order[0])
  files.push([order[0], `@'input(0)`])
if (order[1])
  files.push([order[1], `@'input(1)`])
if (order[2])
  files.push([order[2], `@'input(2)`])
if (order[3])
  files.push([order[3], `@'input(3)`])
if (order[4])
  files.push([order[4], `@'input(4)`])
if (order[5])
  files.push([order[5], `@'input(5)`])
if (order[6])
  files.push([order[6], `@'input(6)`])
if (order[7])
  files.push([order[7], `@'input(7)`])
if (order[8])
  files.push([order[8], `@'input(8)`])
if (order[9])
  files.push([order[9], `@'input(9)`])


send.handle("input", (e) => {
    CodeRunner.send(uid, {stdin: e}, send)
})
send.handle("stop",  (e) => {
    CodeRunner.send(uid, {stop: true}, send)
});


CodeRunner.handle(uid, function (msg) {
    switch (msg.service) {
        case 'data': {
            if (msg.ok) {
                CodeRunner.send(uid, {compile: @2}, send)
            }
            else {
                send.lia("LIA: stop")
            }
            break;
        }
        case 'compile': {
            if (msg.ok) {
                if (msg.message) {
                    if (msg.problems.length)
                        console.warn(msg.message);
                    else
                        console.log(msg.message);
                }

                send.lia("LIA: terminal")
                CodeRunner.send(uid, {exec: @3, filter: pattern})

                if(!@0) {
                  console.clear()
                }
            } else {
                send.lia(msg.message, msg.problems, false)
                send.lia("LIA: stop")
            }
            break;
        }
        case 'stdout': {
            if (msg.ok)
                console.stream(msg.data)
            else
                console.error(msg.data);
            break;
        }

        case 'stop': {
            if (msg.error) {
                console.error(msg.error);
            }

            if (msg.images) {
                for(let i = 0; i < msg.images.length; i++) {
                    console.html("<hr/>", msg.images[i].file)
                    console.html("<img title='" + msg.images[i].file + "' src='" + msg.images[i].data + "' onclick='window.LIA.img.click(\"" + msg.images[i].data + "\")'>")
                }
            }

            if (msg.videos) {
                for(let i = 0; i < msg.videos.length; i++) {
                    console.html("<hr/>", msg.videos[i].file)
                    console.html("<video controls style='width:100%' title='" + msg.videos[i].file + "' src='" + msg.videos[i].data + "'></video>")
                }
            }

            if (msg.files) {
                let str = "<hr/>"
                for(let i = 0; i < msg.files.length; i++) {
                    str += `<a href='data:application/octet-stream${msg.files[i].data}' download="${msg.files[i].file}">${msg.files[i].file}</a> `
                }

                console.html(str)
            }

            window.console.warn(msg)

            send.lia("LIA: stop")
            break;
        }

        default:
            console.log(msg)
            break;
    }
})


CodeRunner.send(
    uid, { "data": files }, send, true
);

"LIA: wait"
</script>
@end

-->

<!--
nvm use v14.21.1
liascript-devserver --input README.md --port 3001 --live
-->


[![LiaScript](https://raw.githubusercontent.com/LiaScript/LiaScript/master/badges/course.svg)](https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2025b/main/classes/14/README.md)


# Programação Lógica (3)





> Este material é parte uma introdução ao paradigma de **programação lógica** em linguagem Prolog.
>
> O conteúdo tem partes interativas e pode ser visualizado de vários modos usando as opções no topo da página.


## Quizzes sobre a prática

- Questões sobre a parte [Escreva suas consultas](#3)
- Questões sobre a parte: [Adicione regras](#4)

### Primeira parte: consultas

A parte "Escreva suas consultas" na prática da aula passada tinha **7 questões**. A seguir temos um quiz sobre 3 delas.

#### 1. Filmes e anos

Qual das alternativas abaixo responde a pergunta: "Quais os filmes lançados na década de 80 (entre 1981 e 1990, inclusive)?"

- [( )] `?- movie(Id,M,A), A >= 1981, A <= 1990.`
- [( )] `?- movie(Id,M,A), A >= 1981, A =< 1990`
- [(x)] `?- movie(Id,M,A), A >= 1981, A =< 1990.`
- [( )] `?- movie(Id,M,A), A >= 1981, A <= 1990`
***********************

Prolog usa o operador relacional `=<` para verificar se um valor é igual ou menor que outro. Além disso, as cláusulas devem terminar com um `.`

Exemplo de resultado:

```
?- movie(Id,M,A), A >= 1981, A =< 1990.
Id = 7,
M = dead_poets_society,
A = 1989 ;
false.
```

***********************








#### 2. Atores ou atrizes

Qual das alternativas abaixo responde corretamente à pergunta: "Quais os atores ou atrizes do filme `the_hunger_games`"?

- [( )] `?- movie(Id,the_hunger_games,_), actor(Id,A), actress(Id,A).`
- [( )] `?- movie(Id,the_hunger_games,_), actor(Id,Actor); actress(Id,Actress).`
- [( )] `?- movie(Id,the_hunger_games,_), actor(Id,A); actress(Id,A).`
- [(x)] `?- movie(Id,the_hunger_games,_), (actor(Id,Actor);actress(Id,Actress)).`
***********************

Prolog usa `;` para expressar um "ou" lógico. As condições são avaliadas da esquerda para a direita. Nesta consulta, usamos parênteses para expressar que o "ou" se refere às condições com os predicados actress e actor. Sem os parênteses, a consulta retornaria ficaria equivalente a `(movie(Id,the_hunger_games,_), (actor(Id,Actor));actress(Id,Actress)).` (devido à avaliação da esquerda para a direita).

Exemplo de resultado:

```
?- movie(Id,the_hunger_games,_), (actor(Id,Actor);actress(Id,Actress)).
Id = 3,
Actor = josh_hutcherson ;
Id = 3,
Actor = liam_hemsworth ;
Id = 3,
Actress = jennifer_lawrence.
```

***********************





#### 3. Ano de lançamento
      


A consulta abaixo responde à pergunta: "Há quantos anos foi lançado o filme `big_fish`"?

```
?- movie(big_fish), Idade is 2025 - A.
```

- [( )] Sim
- [(x)] Não
***********************

Não, porque o predicado `movie` tem 3 argumentos, mas nesse exemplo só foi passado um. Além disso, a variável `A` não poderá ser deduzida.

A seguir uma consulta correta para responder à pergunta:

```
?- movie(_,big_fish,A), Idade is 2025 - A.
A = 2003,
Idade = 21.
```

***********************


### Segunda parte: regras

A parte "Adicione regras em movies.pl" da prática tinha 3 questões. A seguir temos um quiz sobre 2 delas.


#### 4. `drama_artist`

Qual das opções abaixo **não** define corretamente o predicado `drama_artist(A)`, que será verdadeiro se `A` for ator ou atriz em um filme de drama. 

- [( )] `drama_artist(A) :- (actor(M, A); actress(M, A)), genre(M, drama).`
- [(x)] `drama_artist(A) :- genre(M, drama), actor(M, A), actress(M, A).`
- [( )] `drama_artist(A) :- genre(M, drama), (actor(M,A); actress(M,A)).`
***********************

Este predicado precisa de um "ou" (`;`) entre os predicados `actor` e `actress`.

***********************




#### 5. `recommend`



Complete abaixo o predicado `recommend(U,M)`, para recomendar um filme de nome `M` a um usuário `U`. 

Este predicado será verdadeiro se for encontrado um filme do mesmo gênero de um filme apreciado (`like`) pelo usuário `U`. 

Sua resposta deve ser preenchida no campo marcado com "?".

recommend(U,M) :- likes(U,A), genre(A,G), movie(I,M,_), genre(I, [[ G ]]), not(A=I).
***********************

Este predicado:

1. busca um filme `A` apreciado pelo usuário `U` (`likes(U,A)`)
2. obtém o gênero `G` do filme `A` (`genre(A,G)`)
3. busca um filme `I` de nome `M`, desprezando sua data de lançamento (`movie(I,M,_)`)
4. verifica se o gênero de `I` é `G`, ou seja, igual ao gênero de `A` (`genre(I,G)`)
5. impõe a condição `not(A=I)`, para garantir que a recomendação seja de um filme diferente daquele que já consta como apreciado pelo usuário

***********************









## Prática: `songs.pl`


Nos exercícios seguintes, você vai trabalhar com oo arquivo `songs.pl`.

- Baixe o programa [songs.pl](src/songs.pl) e adicione-o no seu Codespace de Prolog:

  ``` bash
  wget https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2025b/main/classes/14/src/songs.pl
  ```

- Note que este código em Prolog declara os seguintes predicados:

  - `song/2`: fatos na forma `song(SongName, Year)`
  - `duration/3`: fatos na forma `duration(SongName, Minutes, Seconds)`
  - `artist/2`: fatos na forma `artist(SongName, ArtistName)`

``` prolog
:- dynamic 
     song/2,
     duration/3,
     artist/2.
:- discontiguous
     song/2,
     duration/3,
     artist/2.


song(thats_my_way,2013).
duration(thats_my_way,5,39).
artist(thats_my_way,edi_rock).

song(sweater_weather,2013).
duration(sweater_weather,4,0).
artist(sweater_weather,the_neighbourhood).

song(feel_the_pop,2024).
duration(feel_the_pop,2,56).
artist(feel_the_pop,zerobaseone).

song(the_emptiness_machine,2024).
duration(the_emptiness_machine,3,10).
artist(the_emptiness_machine,linkin_park).

song(miss_you,2022).
duration(miss_you,3,26).
artist(miss_you,oliver_tree).


song(apenas_um_rapaz_latino_americano,1976).
duration(apenas_um_rapaz_latino_americano,4,18).
artist(apenas_um_rapaz_latino_americano,belchior).

song(poesia_acustica_7_ceu_azul,2019).
duration(poesia_acustica_7_ceu_azul,9,32).
artist(poesia_acustica_7_ceu_azul,mc_hariel).

song(while_your_lips_are_still_red,2008).
duration(while_your_lips_are_still_red,4,18).
artist(while_your_lips_are_still_red,nightwish).


song(tempo_ruim,2006).
duration(tempo_ruim,2,42).
artist(tempo_ruim,matanza).


song(feeling_whitney,2016).
duration(feeling_whitney,4,17).
artist(feeling_whitney,post_malone).

song(just,1995).
duration(just,3,54).
artist(just,radiohead).

song(i_need_u,2015).
duration(i_need_u,3,30).
artist(i_need_u,bts).


song(i_kissed_a_girl,2008).
duration(i_kissed_a_girl,2,59).
artist(i_kissed_a_girl,katy_perry).

song(die_with_a_smile,2024).
duration(die_with_a_smile,4,11).
artist(die_with_a_smile,lady_gaga).
```
@LIA.prolog_withShell




### 1. Duração da música

No final do arquivo `songs.pl`:

- Defina o predicado `duration_in_secs(SongName, Secs)`, de forma que `Secs` seja a duração da música `SongName` em segundos. Lembre de usar `is` para aritmética em Prolog.

- Adicione o seguinte predicado para testar seu código:

  ``` prolog
  playlist_duration(Time) :-
    findall(S, duration_in_secs(_, S), L), sum_list(L, Time). 
  ```

  Se tudo estiver certo, você poderá executar a seguinte consulta:

  ```
  ?- playlist_duration(T).
  T = 3532.
  ```


### 2. Filter (recursão)

- Adicione o predicado recursivo `filterShorts(L1, L2)`, em que `L1` é uma lista de números `L1` e  `L2` é uma lista contendo somente os números menores que `200` de `L1`.

  ``` prolog
  filterShorts([],[]).
  filterShorts(L1,L2) :-
    L1 = [H|T],
    H < 200,
    filterShorts(T, Aux),
    L2 = [H|Aux].
    
  filterShorts(L1,L2) :-
    L1 = [H|T],
    H >= 200,
    filterShorts(T, L2).
  ```

   Explicação:

   - Este predicado é definido com um fato e duas regras.
   - O fato serve como condição de parada da recursão: uma filtragem de lista vazia é uma lista vazia.
   - A notação `[H|T]` unifica com uma lista não-vazia, sendo `H` o primeiro elemento (head) da lista e `T` o restante (tail). Podemos usar esta notação para "decompor" uma lista em head e tail.
   - As duas regras expressam condições alternativas que podem ocorrer. Essas regras poderiam ser combinadas em uma única regra com um `;` (ou), mas ficaria menos legível.

- Adicione o seguinte predicado para testar seu código:

  ```prolog
  playlist_shorts(L) :- 
    findall(S, duration_in_secs(_, S), A), filterShorts(A, L).
  ```

  Se tudo foi adicionado corretamente, você deverá poder executar a seguinte consulta:

  ```
  ?- playlist_shorts(L).
  L = [176, 190, 162, 179]
  ```


### 3. Músicas curtas (consulta)

- Escreva uma consulta para obter os nomes das músicas cuja duração seja menor que 200 segundos.

### 4. Músicas curtas (regra)

- Adicione um predicado `short(Song)` que seja verdadeiro se `Song` for uma música com duração menor que 200 segundos.




## Bibliografia


- Robert Sebesta. Conceitos de Linguagens de Programação. Bookman, 2018. Disponível no Portal de E-books da UFSM: http://portal.ufsm.br/biblioteca/leitor/minhaBiblioteca.html (Capítulo 16: Programação Lógica)
- Patrick Blackburn, Johan Bos, and Kristina Striegnitz. [Learn Prolog Now](http://www.learnprolognow.org).
- Markus Triska. [The Power of Prolog](https://www.metalevel.at/prolog).
