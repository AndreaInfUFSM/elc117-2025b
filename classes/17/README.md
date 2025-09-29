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
@LIA.haskell:           @LIA.eval(`["main.hs"]`, `ghc main.hs -o main`, `./main`)
@LIA.haskell_withShell: @LIA.eval(`["main.hs"]`, `none`, `ghci main.hs`)

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


[![LiaScript](https://raw.githubusercontent.com/LiaScript/LiaScript/master/badges/course.svg)](https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2025b/main/classes/17/README.md)


# Resultados: leitura de código

- 28 estudantes / 33 matriculados
- 6 questões: 4 Haskell (4,0) + 2 Prolog (1,0)
- média (simulada) com notas 0 ou 1: 3.2/5 (64%)
- média com notas parciais, graças às explicações: **acima de 7**!



## Q5a

<svg viewBox="0 0 473 20" width="100%" height="20" role="img" aria-label="Resultados da questão 5a, 28 estudantes">
  <!-- cell size 14x16, gap 3, rx 2 -->
  <rect x="0"   y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="17"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="34"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="51"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="68"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="85"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="102" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="119" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="136" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="153" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="170" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="187" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="204" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="221" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="238" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="255" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="272" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="289" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="306" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="323" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="340" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="357" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="374" y="2" width="14" height="16" rx="2" fill="#ef4444" />
  <rect x="391" y="2" width="14" height="16" rx="2" fill="#ef4444" />
  <rect x="408" y="2" width="14" height="16" rx="2" fill="#ef4444" />
  <rect x="425" y="2" width="14" height="16" rx="2" fill="#ef4444" />
  <rect x="442" y="2" width="14" height="16" rx="2" fill="#ef4444" />
  <rect x="459" y="2" width="14" height="16" rx="2" fill="#ef4444" />
</svg>


``` prolog
prop(m,a,10).
prop(m,b,12).
prop(n,a,11).
prop(n,b,15).
allprops([10,11,12,13,14,15]).

rule1(S) :- prop(S, a, N), allprops([H|_]), H = N.
```
@LIA.prolog_withShell

> `?- rule1(S).`


## Q5b


<svg viewBox="0 0 473 20" width="100%" height="20" role="img" aria-label="Resultados da questão 5a, 28 estudantes">
  <!-- cell size 14x16, gap 3, rx 2 -->
  <rect x="0"   y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="17"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="34"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="51"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="68"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="85"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="102" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="119" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="136" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="153" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="170" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="187" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="204" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="221" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="238" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="255" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="272" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="289" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="306" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="323" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="340" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="357" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="374" y="2" width="14" height="16" rx="2" fill="#ef4444" />
  <rect x="391" y="2" width="14" height="16" rx="2" fill="#ef4444" />
  <rect x="408" y="2" width="14" height="16" rx="2" fill="#ef4444" />
  <rect x="425" y="2" width="14" height="16" rx="2" fill="#ef4444" />
  <rect x="442" y="2" width="14" height="16" rx="2" fill="#ef4444" />
  <rect x="459" y="2" width="14" height="16" rx="2" fill="#ef4444" />
</svg>

``` prolog
prop(m,a,10).
prop(m,b,12).
prop(n,a,11).
prop(n,b,15).
allprops([10,11,12,13,14,15]).

rule2(S,R) :- prop(S,a,A), prop(S,b,B), R is A*B.
```
@LIA.prolog_withShell

> `?- rule2(n,11).`

## Q1

<svg viewBox="0 0 473 20" width="100%" height="20" role="img" aria-label="Resultados da questão 5a, 28 estudantes">
  <!-- cell size 14x16, gap 3, rx 2 -->
  <rect x="0"   y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="17"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="34"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="51"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="68"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="85"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="102" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="119" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="136" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="153" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="170" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="187" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="204" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="221" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="238" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="255" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="272" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="289" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="306" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="323" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="340" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="357" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="374" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="391" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="408" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="425" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="442" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="459" y="2" width="14" height="16" rx="2" fill="#ef4444" />
</svg>


``` haskell
questao1 :: [String] -> [String] -> Int -> [String]
questao1 xs ys n = 
  [f x y | x <- xs, y <- ys, length x + length y + lenc < n]
  where
    c = " the " -- String inicia e termina com espaço
    lenc = length c
    f x y = x ++ c ++ y

resposta1 = questao1 ["Sofia", "Homero"] ["brave", "strong", "wise"] 15

main = do
  print resposta1
```
@LIA.haskell

## Q2


<svg viewBox="0 0 473 20" width="100%" height="20" role="img" aria-label="Resultados da questão 5a, 28 estudantes">
  <!-- cell size 14x16, gap 3, rx 2 -->
  <rect x="0"   y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="17"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="34"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="51"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="68"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="85"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="102" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="119" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="136" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="153" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="170" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="187" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="204" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="221" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="238" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="255" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="272" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="289" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="306" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="323" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="340" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="357" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="374" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="391" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="408" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="425" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="442" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="459" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
</svg>


``` haskell
questao2 :: [Int] -> [Int]
questao2 ns = map myfun ns
  where
    myfun n
      | n < 0     = (-n)
      | odd n     = n * n
      | otherwise = 0

resposta2 = length $ questao2 [1,2,3,4,-4,-3,-2,-1]

main = do
  print resposta2
```
@LIA.haskell


## Q3


<svg viewBox="0 0 473 20" width="100%" height="20" role="img" aria-label="Resultados da questão 5a, 28 estudantes">
  <!-- cell size 14x16, gap 3, rx 2 -->
  <rect x="0"   y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="17"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="34"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="51"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="68"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="85"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="102" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="119" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="136" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="153" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="170" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="187" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="204" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="221" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="238" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="255" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="272" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="289" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="306" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="323" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="340" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="357" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="374" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="391" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="408" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="425" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="442" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="459" y="2" width="14" height="16" rx="2" fill="#ef4444" />
</svg>


``` haskell
questao3 :: [Int] -> Bool
questao3 xs = 
  length (filter (\n -> n `mod` 3 == 0) xs) == length xs

resposta3 = questao3 [1,2,3,4,5]

main = do
  print resposta3
```
@LIA.haskell


## Q4


<svg viewBox="0 0 473 20" width="100%" height="20" role="img" aria-label="Resultados da questão 5a, 28 estudantes">
  <!-- cell size 14x16, gap 3, rx 2 -->
  <rect x="0"   y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="17"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="34"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="51"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="68"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="85"  y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="102" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="119" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="136" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="153" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="170" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="187" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="204" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="221" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="238" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="255" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="272" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="289" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="306" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="323" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="340" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="357" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="374" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="391" y="2" width="14" height="16" rx="2" fill="#10b981" />
  <rect x="408" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="425" y="2" width="14" height="16" rx="2" fill="#f59e0b" />
  <rect x="442" y="2" width="14" height="16" rx="2" fill="#ef4444" />
  <rect x="459" y="2" width="14" height="16" rx="2" fill="#ef4444" />
</svg>


``` haskell
questao4 :: [String] -> [Int] -> [(String, String)]
questao4 ss ns =
  zipWith myfun ss ns
  where myfun s n = (s, if n >= 20 then "A" else "B")

resposta4 = questao4 ["Oslo", "Paris", "Cairo", "Manaus"] [5, 18, 32, 24]

main = do
  print resposta4
```
@LIA.haskell


