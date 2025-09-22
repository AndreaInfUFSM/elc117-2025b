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


[![LiaScript](https://raw.githubusercontent.com/LiaScript/LiaScript/master/badges/course.svg)](https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2025b/main/classes/15/README.md)


# Programação Lógica (4)





> Este material é parte uma introdução ao paradigma de **programação lógica** em linguagem Prolog.
>
> O conteúdo tem partes interativas e pode ser visualizado de vários modos usando as opções no topo da página.



## Prática

Nesta prática, você vai completar um programa Prolog para desvendar um mistério: a autoria de um crime! 🤪

### O crime

No final de uma festa no oitavo andar do Hotel "Paradigm Palace", o milionário Sr. Dollars foi assassinado! Todas as pessoas presentes no final da festa foram imediatamente detidas como suspeitas: eram 2 homens (Bob e Dave) e 2 mulheres (Alice e Carol). Cada suspeito estava hospedado em uma destas suítes do oitavo andar: 801, 802, 803 e 804. Em cada suíte, foi encontrada uma arma: Faca, Revólver, Corda e Veneno. As suítes se localizavam em sequência no corredor do oitavo andar, uma ao lado da outra.

Para desvendar este crime, você precisa deduzir onde cada pessoa estava e qual arma foi encontrada em cada suíte. 

As pistas são as seguintes:

1. A pessoa com a Corda estava no quarto 803.
2. Alice estava com o Revólver.
3. Carol estava num quarto de número par.
4. Alice e Bob não estavam em quartos adjacentes.
5. Nenhum dos homens estava com o Veneno.
6. Dave estava com a Faca.
7. Uma das mulheres estava no último quarto.
8. Quem cometeu o assassinato estava hospedado no quarto 802.

> Quem matou o Sr. Dollars e qual arma foi usada?


### O programa

- Obtenha o programa [notel-muder-incompleto.pl](src/hotel-murder-incompleto.pl):

  ``` bash
  wget https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2025b/main/classes/15/src/hotel-murder-incompleto.pl
  ```

- Adicione uma regra `even(N)` para determinar se um número N é par. Dica: Em Prolog, [mod](https://www.swi-prolog.org/pldoc/man?function=mod/2) serve para calcular o resto de uma divisão inteira.

- O programa fornecido não expressa todas as condições/pistas. Descubra qual está faltando e adicione-a no programa.


``` prolog

solution(Rooms, Assassin, Weapon) :-
  Rooms = [room(801, _, _), room(802, _, _), room(803, _, corda), room(804, _, _)],  
  member(room(_, alice, revolver), Rooms), 
  member(room(_, bob, _), Rooms),     
  member(room(N, carol, _), Rooms),   
  even(N),                           
  not(nextto(room(_, alice, _),room(_, bob, _), Rooms)), 
  not(nextto(room(_, bob,_), room(_, alice, _), Rooms)), 
  member(room(_, _, veneno), Rooms),  
  not(member(room(_, dave, veneno), Rooms)),
  not(member(room(_, bob, veneno), Rooms)),            
  (member(room(804, carol, _), Rooms); member(room(804, alice, _), Rooms)), 
  member(room(802, Assassin, Weapon), Rooms).
```
@LIA.prolog_withShell

## Quiz

1. Considerando a base de fatos e regras da aula passada ([songs.pl](https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2025b/main/classes/14/src/songs.pl)), o que faz a consulta abaixo?


   ``` prolog
   ?- duration(Song,M,S), Secs is M*60 + S, Secs < 200.
   ```

   - [( )] Causa um erro devido a uma variável não definida.
   - [(x)] Obtém todos dados de músicas cuja duração é menor que 200 segundos.
   - [( )] Obtém apenas os nomes das músicas cuja duração é menor que 200 segundos.


2. Suponha que se deseje obter apenas os nomes de todas as músicas com 3 minutos ou mais de duração. Qual regra pode ser definida para auxiliar nisso?

   - [(x)] `regra(Song) :- duration(Song, M, _), M >= 3.`
   - [( )] `regra(M) :- duration(Song, M, _), M >= 3.`
   - [( )] `regra(Song) :- duration(Musica, Minutos, _), Minutos >= 3.`


3. Qual será o resultado da consulta `?- [3,2,1] = [A|B].`?

   - [( )] [1, 2, 3]
   - [( )] false
   - [(x)] A = 3, B = [2, 1]


4. Qual será o resultado da consulta `?- [4,5,6] = [4,5,L].`?

   - [( )] true
   - [(x)] L = 6
   - [( )] [6]
   - [( )] false

5. Qual será o resultado da consulta `?- [4,5,6] = [4,5,L,6].`?

   - [( )] true
   - [( )] L = 6
   - [( )] [6]
   - [(x)] false

## Listas com `[H|T]`

Sintaxe `[H|T]` é usada para representar uma lista **não-vazia** onde:

- Caracter `|` expressa uma separação/decomposição de lista
- `H` designa o primeiro elemento da lista, chamado de "cabeça" (head)
- `T` designa o restante da lista, chamado de "cauda" (head), que é outra lista (possivelmente vazia), excluindo o primeiro elemento


Exemplos:

- `[1, 2, 3]` equivale a `[1 | [2, 3]]`, sendo 1 a cabeça da lista e [2, 3] a cauda
- `[a, b, c]` equivale a `[H | T]` se H = a e T = [b,c]
- `[1, 2]` equivale a `[X | Y]` se X = 1 e Y = [2]
- `[movie(the_avengers)]` equivale a `[H | T]` se H = movie(the_avengers) e T = []

No interpretador SWI-Prolog:

```
?- A=[1|[2,3]].
A = [1, 2, 3].

?- [a,b,c]=[H|T].
H = a,
T = [b, c].

?- [a,b,c]=[H|T].
H = a,
T = [b, c].

?- [1,2]=[X|Y].
X = 1,
Y = [2].

?- [H|T]=[movie(the_avengers)].
H = movie(the_avengers),
T = [].
```

### Usos de `[H|T]`


- Manipulação de listas: 

  - sintaxe `[H|T]` é usada para decompor listas e acessar seus elementos


- Construção de listas:

  - sintaxe `[H|T]`  também pode ser usada para construir listas
  - por exemplo, [1, 2, 3] pode ser construída como `L = [1|[2|[3|[]]]]`, onde `[]` representa uma lista vazia.


- Recursão: 

  - sintaxe `[H|T]` costuma ser usada em recursão
  - por exemplo, regras que obtêm `H` e aplicam a própria regra com `T` até atingir a lista vazia (`[]`).

- Unificação com padrões: 

  - sintaxe `[H|T]` pode ser usada para verificar se uma lista corresponde a um padrão específico



### Exemplos com `[H|T]`

Avance para ver alguns exemplos de regras que usam `[H|T]`.


#### `hasEqHeads/2`

Regra que verifica se duas listas têm a mesma "head"

Definição:

``` prolog
hasEqHeads(L1, L2) :-
  L1 = [H1|_],
  L2 = [H2|_],
  H1 = H2.
```

Alternativa abreviada:

``` prolog
hasEqHeads([H|_], [H|_]).
```

Uso no SWI-Prolog:

``` prolog
?- hasEqHeads([1,2,3],[2,3,4]).
false.

?- hasEqHeads([a,2,3],[a,3,4]).
true.
```


#### `getHeads/3`

Regra que gera lista com "head"s de outras 2 listas:


``` prolog
getHeads(L1, L2, L3) :-
  L1 = [H1|_],
  L2 = [H2|_],
  L3 = [H1, H2].
```

Alternativa abreviada:

``` prolog
getHeads([H1|_], [H2|_], [H1, H2]).
```


Uso no SWI-Prolog:

``` prolog
?- getHeads([1,2],[4,5],L).
L = [1,4].
```


#### `doubleHead/2`

Regra que gera nova lista com quadrado do "head" da primeira lista

``` prolog
doubleHead(L1,L2) :-
  L1 = [H|T],
  D is H*H,
  L2 = [D|T].
``` 

Alternativa abreviada:

``` prolog
doubleHead([H|T],[H*H|T]).
``` 


Uso no SWI-Prolog:

``` prolog
?- doubleHead([2,3,4],L).
L = [4,3,4].
```



### Recursão com `[H|T]`

A seguir, alguns exemplos de uso da notação `[H|T]` para expressar predicados recursivos.

#### `sum/2`

Somatório de elementos de uma lista

``` prolog
sum(L,S) :-
  L = [],
  S = 0.

sum(L, S) :-
   L = [H|T],
   sum(T, S1),
   S is H + S1.
```

Alternativa abreviada:

``` prolog
sum([],0).

sum([H|T], S) :-
   sum(T, S1),
   S is H + S1.
```


#### `tamanho/2`

Quantidade de elementos de uma lista

``` prolog
tamanho([], 0).
tamanho([_|T], N) :-
  tamanho(T, X),
  N is X + 1.
```

Uso no SWI-Prolog:

```
?- tamanho([],T).
T = 0.

?- tamanho([1,2,3],T).
T = 3.

?- tamanho([1,2,3,a,b,c],T).
T = 6.

?- tamanho([a,b,c],T).
T = 3.

?- tamanho([a,b,c,1,2,3],T).
T = 6.
```


#### `ultimo/2`

Último elemento de uma lista

- se lista tem um elemento: último da lista é H
- senão: último da lista é último de T

``` prolog
ultimo([H], H).
ultimo([_|T], U) :-
  ultimo(T, U).
```


#### `mapSquare/2`

Gera nova lista com todos elementos da primeira lista elevados ao quadrado


``` prolog
mapSquare([],[]).
mapSquare([H|T],L) :-
  D is H*H,
  L = [D | R],
  mapSquare(T,R).
```


#### `filterEven/2`

Filtra elementos pares de uma lista 

``` prolog
even(N) :- 
  R is N mod 2,
  R = 0.
  
filterEven([],[]).
filterEven([H|T],L) :-
  even(H),  
  filterEven(T,Aux),
  L = [H|Aux].

filterEven([H|T],L) :-
  not(even(H)),  
  filterEven(T,L).
```




## Bibliografia


- Robert Sebesta. Conceitos de Linguagens de Programação. Bookman, 2018. Disponível no Portal de E-books da UFSM: http://portal.ufsm.br/biblioteca/leitor/minhaBiblioteca.html (Capítulo 16: Programação Lógica)
- Patrick Blackburn, Johan Bos, and Kristina Striegnitz. [Learn Prolog Now](http://www.learnprolognow.org).
- Markus Triska. [The Power of Prolog](https://www.metalevel.at/prolog).
