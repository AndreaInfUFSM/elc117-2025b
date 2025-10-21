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

script:   https://cdn.jsdelivr.net/npm/mermaid@10.5.0/dist/mermaid.min.js

@load.java: @load(java,@0)

@load
<script style="display: block" modify="false" run-once="true">
    fetch("@1")
    .then((response) => {
        if (response.ok) {
            response.text()
            .then((text) => {
                send.lia("LIASCRIPT:\n``` @0\n" + text + "\n```")
            })
        } else {
            send.lia("HTML: <span style='color: red'>Something went wrong, could not load <a href='@1'>@1</a></span>")
        }
    })
    "loading: @1"
</script>
@end

@onload
mermaid.initialize({ startOnLoad: false });
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


@LIA.java:              @LIA.eval(`["@0.java"]`, `javac @0.java`, `java @0`)
@LIA.c:                 @LIA.eval(`["main.c"]`, `gcc -Wall main.c -o a.out`, `./a.out`)

@LIA.eval:  @LIA.eval_(false,`@0`,@1,@2,@3)

@LIA.evalWithDebug: @LIA.eval_(true,`@0`,@1,@2,@3)


@mermaid: @mermaid_(@uid,```@0```)

@mermaid_
<script run-once="true" modify="false" style="display:block; background: white">
async function draw () {
    const graphDefinition = `@1`;
    const { svg } = await mermaid.render('graphDiv_@0', graphDefinition);
    send.lia("HTML: "+svg);
    send.lia("LIA: stop")
};

draw()
"LIA: wait"
</script>
@end

@mermaid_eval: @mermaid_eval_(@uid)

@mermaid_eval_
<script>
async function draw () {
    const graphDefinition = `@input`;
    const { svg } = await mermaid.render('graphDiv_@0', graphDefinition);
    console.html(svg);
    send.lia("LIA: stop")
};

draw()
"LIA: wait"
</script>
@end

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

[![LiaScript](https://raw.githubusercontent.com/LiaScript/LiaScript/master/badges/course.svg)](https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2025b/main/classes/23/README.md)



# Programação Orientada a Objetos (6)




> Este material faz parte de uma introdução ao paradigma de **programação orientada a objetos** em linguagem Java.


## Conceitos da POO

![](img/oo-poly.png)


**Relembrando:** A programação orientada a objetos se baseia em alguns conceitos (independentes de sintaxe) que favorecem organização, manutenção, compreensão e reuso de código. 

Conceitos básicos:

- [ ] Encapsulamento: https://en.wikipedia.org/wiki/Encapsulation_%28computer_programming%29

- [ ] Herança: https://en.wikipedia.org/wiki/Inheritance_%28object-oriented_programming%29

- [ ] Polimorfismo: https://en.wikipedia.org/wiki/Polymorphism_%28(computer_science%29







## Relembrando: herança

- Classes derivadas (declaradas com `extends`)

  - herdam (reusam!) atributos e métodos de classes existentes

- Construtor da superclasse 

  - é chamado antes do construtor da classe derivada

- Visibilidade `protected`

  - torna atributos/métodos acessíveis a classe derivada



### Resumo de visibilidade

| Acesso     | private   | protected   | public   | default   |
| :--------- | :--------- | :--------- | :--------- | :--------- |
| mesma classe                        | sim    | sim     | sim     | sim     |
| classe derivada (mesmo pacote)      | não    | sim     | sim     | sim     |
| classe não derivada (mesmo pacote)  | não    | sim     | sim     | sim     |
| classe derivada (outro pacote)      | não    | sim     | sim     | não     |
| classe não derivada (outro pacote)  | não    | não     | sim     | não     |


### Exemplo básico

Observações importantes:

1. `Student` deriva de `Person` (estudante é uma pessoa)
2. método público `getName` de `Person` é herdado por `Student`
3. `getName` continua público em `Student` e pode ser chamado por classes usuárias
4. `new` provoca chamada de construtores


``` java
class Person {
  private String name;
  public Person() {    
    this.name = "empty name";
    System.out.println("Person constructor");
  }
  public String getName() {
    return this.name;
  }
}
class Student extends Person {
  private String course;
  public Student() {    
    this.course = "default course";
    System.out.println("Student constructor");
  }
}
public class Main {
  public static void main(String[] args) {
    Person p = new Person();
    Student s = new Student();
    System.out.println(s.getName());
  }
}
```

### Execução passo-a-passo

Python Tutor: Online Compiler, Visual Debugger, and AI Tutor for Python, Java, C, C++, and JavaScript 

Execute o código passo-a-passo neste [link](https://pythontutor.com/visualize.html#code=class%20Person%20%7B%0A%20%20private%20String%20name%3B%0A%20%20public%20Person%28%29%20%7B%20%20%20%20%0A%20%20%20%20this.name%20%3D%20%22empty%20name%22%3B%0A%20%20%20%20System.out.println%28%22Person%20constructor%22%29%3B%0A%20%20%7D%0A%20%20public%20String%20getName%28%29%20%7B%0A%20%20%20%20return%20this.name%3B%0A%20%20%7D%0A%7D%0Aclass%20Student%20extends%20Person%20%7B%0A%20%20private%20String%20course%3B%0A%20%20public%20Student%28%29%20%7B%20%20%20%20%0A%20%20%20%20this.course%20%3D%20%22default%20course%22%3B%0A%20%20%20%20System.out.println%28%22Student%20constructor%22%29%3B%0A%20%20%7D%0A%7D%0Apublic%20class%20Main%20%7B%0A%20%20public%20static%20void%20main%28String%5B%5D%20args%29%20%7B%0A%20%20%20%20Person%20p%20%3D%20new%20Person%28%29%3B%0A%20%20%20%20Student%20s%20%3D%20new%20Student%28%29%3B%0A%20%20%20%20System.out.println%28s.getName%28%29%29%3B%0A%20%20%7D%0A%7D&cumulative=false&heapPrimitives=true&mode=edit&origin=opt-frontend.js&py=java&rawInputLstJSON=%5B%5D&textReferences=false)

![](img/java-tutor.gif)


### Exemplo com `super`

Palavra-chave `super` deve ser seguida de `(` ou de `.`:

- `super(`: uso apenas no início de construtor da subclasse para invocar construtor da superclasse (JDK 25 flexibilizou isso: https://openjdk.org/jeps/513)
- `super.`: uso em subclasse para designar atributo/método da superclasse


No código abaixo, construtor de Student invoca construtor de Person usando `super(`

Execute o código passo-a-passo neste [link](https://pythontutor.com/visualize.html#code=class%20Person%20%7B%0A%20%20protected%20String%20name%3B%0A%20%20public%20Person%28%29%20%7B%20%20%20%20%0A%20%20%20%20this.name%20%3D%20%22empty%20name%22%3B%0A%20%20%20%20System.out.println%28%22Person%20constructor%201%22%29%3B%0A%20%20%7D%0A%20%20public%20Person%28String%20name%29%20%7B%20%20%20%20%0A%20%20%20%20this.name%20%3D%20name%3B%0A%20%20%20%20System.out.println%28%22Person%20constructor%202%22%29%3B%0A%20%20%7D%0A%20%20public%20String%20getName%28%29%20%7B%0A%20%20%20%20return%20name%3B%0A%20%20%7D%0A%7D%0Aclass%20Student%20extends%20Person%20%7B%0A%20%20private%20String%20course%3B%0A%20%20public%20Student%28%29%20%7B%20%20%20%20%0A%20%20%20%20course%20%3D%20%22default%20course%22%3B%0A%20%20%20%20System.out.println%28%22Student%20constructor%201%22%29%3B%0A%20%20%7D%0A%20%20public%20Student%28String%20name%29%20%7B%0A%20%20%20%20super%28name%29%3B%20%20%20%20%0A%20%20%20%20course%20%3D%20%22default%20course%22%3B%0A%20%20%20%20System.out.println%28%22Student%20constructor%202%22%29%3B%0A%20%20%7D%0A%7D%0Apublic%20class%20Main%20%7B%0A%20%20public%20static%20void%20main%28String%5B%5D%20args%29%20%7B%0A%20%20%20%20Person%20p%20%3D%20new%20Person%28%22Alice%22%29%3B%0A%20%20%20%20Student%20s%20%3D%20new%20Student%28%22Bob%22%29%3B%0A%20%20%20%20System.out.println%28s.getName%28%29%29%3B%0A%20%20%7D%0A%7D&cumulative=false&heapPrimitives=true&mode=edit&origin=opt-frontend.js&py=java&rawInputLstJSON=%5B%5D&textReferences=false)

``` java
class Person {
  protected String name;
  public Person() {    
    this.name = "empty name";
    System.out.println("Person constructor 1");
  }
  public Person(String name) {    
    this.name = name;
    System.out.println("Person constructor 2");
  }
  public String getName() {
    return name;
  }
}
class Student extends Person {
  private String course;
  public Student() {    
    course = "default course";
    System.out.println("Student constructor 1");
  }
  public Student(String name) {
    super(name);    
    course = "default course";
    System.out.println("Student constructor 2");
  }
}
public class Main {
  public static void main(String[] args) {
    Person p = new Person("Alice");
    Student s = new Student("Bob");
    System.out.println(s.getName());
  }
}
```

### Herança de `Object`

Hierarquia de classes em Java:

- Em Java, todas as classes derivam da classe `Object`
- Na classe `Object` existe uma implementação do método `toString()`
- Método `toString()` retorna uma representação do objeto em forma de `String`


### Exemplo com *override* de `toString`

> Sobrescrita habilita **polimorfismo dinâmico** 

Sobrescrita (*override*):

- Classes derivadas podem sobrescrever (*override*) métodos da superclasse (declarar suas próprias implementações modificadas)
- Para sobrescrever (*override*), declaramos o método com a mesma **assinatura**: mesmo nome, mesmo tipo de retorno e mesmos argumentos
- Sobrescrita de `toString()` é muito comum, mas também podemos sobrescrever qualquer outro método
- Sobrescrita habilita **polimorfismo dinâmico** 

Execute o código passo-a-passo neste [link](https://pythontutor.com/visualize.html#code=class%20Person%20%7B%0A%20%20protected%20String%20name%3B%0A%20%20public%20Person%28%29%20%7B%20%20%20%20%0A%20%20%20%20this.name%20%3D%20%22empty%20name%22%3B%20%20%20%20%0A%20%20%7D%0A%20%20public%20String%20getName%28%29%20%7B%0A%20%20%20%20return%20this.name%3B%0A%20%20%7D%0A%20%20public%20String%20toString%28%29%20%7B%0A%20%20%20%20return%20%22%28%22%20%2B%20this.name%20%2B%20%22%29%22%3B%0A%20%20%7D%0A%7D%0Aclass%20Student%20extends%20Person%20%7B%0A%20%20private%20String%20course%3B%0A%20%20public%20Student%28%29%20%7B%20%20%20%20%0A%20%20%20%20this.course%20%3D%20%22default%20course%22%3B%0A%20%20%7D%0A%20%20public%20String%20toString%28%29%20%7B%0A%20%20%20%20return%20%22%28%22%20%2B%20this.name%20%2B%20%22,%22%20%2B%20this.course%20%2B%20%22%29%22%3B%0A%20%20%7D%0A%7D%0Apublic%20class%20Main%20%7B%0A%20%20public%20static%20void%20main%28String%5B%5D%20args%29%20%7B%0A%20%20%20%20Person%20p%20%3D%20new%20Person%28%29%3B%0A%20%20%20%20Student%20s%20%3D%20new%20Student%28%29%3B%0A%20%20%20%20System.out.println%28p%29%3B%0A%20%20%20%20System.out.println%28s%29%3B%0A%20%20%7D%0A%7D&cumulative=false&heapPrimitives=true&mode=edit&origin=opt-frontend.js&py=java&rawInputLstJSON=%5B%5D&textReferences=false)

``` java
class Person {
  protected String name;
  public Person() {    
    this.name = "empty name";    
  }
  public String getName() {
    return this.name;
  }
  public String toString() {
    return "(" + this.name + ")";
  }
}
class Student extends Person {
  private String course;
  public Student() {    
    this.course = "default course";
  }
  public String toString() {
    return "(" + this.name + "," + this.course + ")";
  }
}
public class Main {
  public static void main(String[] args) {
    Person p = new Person();
    Student s = new Student();
    System.out.println(p); // chama toString de Person
    System.out.println(s); // chama toString de Student
  }
}
```


## Polimorfismo

Segundo a Wikipedia em https://en.wikipedia.org/wiki/Polymorphism_%28computer_science%29 :

> In programming language theory and type theory, polymorphism is the provision of a single interface to entities of different types or the use of a single symbol to represent multiple different types. The concept is borrowed from a principle in biology where an organism or species can have many different forms or stages.


> Resumindo: um mesmo símbolo/nome associado a diferentes implementações


Exemplificando com nossos códigos anteriores:

- *"single interface to entities of different types"*: 

  - em hierarquias de classes: por exemplo, override do método toString em Person e Student (toString é polimórfico)
  - na mesma classe: por exemplo, overload de construtores ou métodos, diferenciados pelos argumentos (construtores e métodos podem ser polimórficos)

- *"single symbol to represent multiple different types"*:

  - em hierarquia de classes: por exemplo, Person pode representar Student, Professor, etc. (Person é polimórfico)
  


### Estático x Dinâmico

| Polimorfismo estático   | Polimorfismo dinâmico   |
| :--------- | :--------- |
| Código a ser executado é determinado em tempo de compilação    | Código a ser executado é determinado em tempo de execução     |
| Sobrecarga (overloading), paramétrico (generics, ex.: ArrayList<Person>) | Sobrescrita (overriding) | 
| Termos relacionados: function/operator overload, templates, generics | Termos relacionados: late binding, dynamic binding, runtime binding | 




### Exemplo sem polimorfismo

Exemplo sem herança nem polimorfismo:

@[load.java](src/nopolymorphism/Main.java)

### Exemplo com polimorfismo dinâmico

Exemplo com herança e polimorfismo dinâmico (*override* do método `desenha`):

@[load.java](src/polymorphism/Main.java)




### Quiz

1. Qual será a saída do código abaixo?

   - [( )] `empty name`
   - [( )] `I'm a Person`
   - [(x)] `I'm a Student`

2. Se trocarmos a linha `p = s` por `s = p`, o código irá compilar sem erro ?

   - [( )] Sim
   - [(x)] Não


@[load.java](src/quiz/Main.java)


### Por quê?

> Polimorfismo: código com menos condicionais e redundância, mais limpo e fácil de testar!

Muitos materiais sugerem que polimorfismo simplifica código ao substituir condicionais:

- https://refactoring.guru/replace-conditional-with-polymorphism
- https://dev.to/tomazlemos/keeping-your-code-clean-by-sweeping-out-if-statements-4in8
- "The Clean Code Talks -- Inheritance, Polymorphism, & Testing": https://www.youtube.com/watch?v=4F72VULWFvc



### Exemplos com *overload* de construtor

Temos 2 construtores na classe Retangulo, diferenciados pelos argumentos:

@[load.java](src/overloadconstructor/Main.java)


Veja também: 

- https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Rectangle.java#L54
- https://java-programming.mooc.fi/part-5/2-method-and-constructor-overloading


### Exemplos com *overload* de método

Temos 2 métodos `grow` na classe Retangulo, diferenciados pelos argumentos:

@[load.java](src/overloadmethod/Main.java)

Veja também: 

https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/math/Rectangle.java#L188

https://java-programming.mooc.fi/part-5/2-method-and-constructor-overloading



## Classes abstratas e interfaces

> Agora que você conhece o básico... veja mais algums recursos

<section class="h3">Exemplo em java.util</section>


Sumário de recursos do pacote java.util:
https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/package-summary.html

O que será isso?

``` java
public abstract class AbstractList<E>
extends AbstractCollection<E>
implements List<E>
```

<section class="h3">Exemplo em com.badlogic.gdx</section>

Uma classe no pacote com.badlogic.gdx da libGDX para games multiplataforma:
https://javadoc.io/doc/com.badlogicgames.gdx/gdx/latest/com/badlogic/gdx/Game.html

O que será isso?

``` java 
public abstract class Game
extends java.lang.Object
implements ApplicationListener
```

### Classes abstratas


- `abstract`: classes "incompletas"
- Não podem ser instanciadas com new (pois são incompletas!)
- Possuem métodos abstratos que devem ser sobrescritos (*override*) nas classes derivadas
- Obrigam as classes derivadas a implementar ("preencher") o que falta ("preencha seu código aqui e reuse o restante")
- Podem ter métodos concretos (implementados) e atributos que serão transmitidos por herança


![Imagem retirada da página sobre Java no site humorístico Desciclopédia. A imagem mostra à esquerda a foto de um leão e à direita um desenho infatil do tipo "ligue os pontos", com um leão desenhado parcialmente. A imagem da esquerda tem o título "concepção real world" e a da direita "concepção Java". O desenho de "ligar os pontos" é uma forma bem-humorada de representar classes abstratas.](img/ConcepJava.PNG)

Fonte: Desciclopédia :-) http://desciclopedia.org/wiki/Arquivo:ConcepJava.PNG

Mais sobre Java na Desciclopédia: http://desciclopedia.org/wiki/Java_%28linguagem_de_programa%C3%A7%C3%A3o%29

### Exemplo básico

Erro: `Figura f = new Figura(); // erro!`


@[load.java](src/abstract/Main.java)



### Exemplo em java.util

- Java Collections Framework possui uma hierarquia de classes de estruturas de dados (coleções/containers)
- Classes abstratas são a base do framework
- Classes concretas especializam e preenchem as classes abstratas

Classe abstrata:

``` java
public abstract class AbstractList<E>
extends AbstractCollection<E>
implements List<E>
```

Classes concretas:


``` java
public class ArrayList<E>
extends AbstractList<E>
implements List<E>, RandomAccess, Cloneable, Serializable
```

``` java
public class LinkedList<E>
extends AbstractSequentialList<E>
implements List<E>, Deque<E>, Cloneable, Serializable
```

Mais recursos do pacote java.util:
https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/package-summary.html




### Exemplo em com.badlogic.gdx

- libGDX: open-source, cross-platform Java game development framework 
- classes abstratas oferecem uma base para criação de games
- jogos são classes concretas que especializam e preenchem as classes abstratas

Classe abstrata: https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/Game.java

``` java
public abstract class Game
implements ApplicationListener
```

Exemplo de classe concreta: https://github.com/elc117/t4-2022a-flavio_gregori_matheus/blob/main/core/src/com/mygdx/game/GuillotineClicker.java


``` java
public class GuillotineClicker 
extends Game
```



Mais recursos do pacote: https://javadoc.io/doc/com.badlogicgames.gdx





### Interfaces

- Uma `interface` define um ou mais métodos (comportamento) que devem ser implementados (`implements`) em classes derivadas

- É semelhante a um encapsulamento contendo apenas métodos abstratos

- É como um "contrato de comportamento": 

  - a interface estabelece um comportamento e a classe implementa este comportamento
  - classes que implementam uma interface garantem que têm um comportamento em comum 

- Permitem criar uma hierarquia mais "leve" (sem preocupação com atributos) e se beneficiar do **polimorfismo dinâmico**




``` java
interface Runnable {
 public void run();
}
```

``` java
class Worker implements Runnable {
 public void run() {
   //...
 }
}
```


- Exemplo em javax.swing: interface Scrollable https://docs.oracle.com/en/java/javase/14/docs/api/java.desktop/javax/swing/Scrollable.html


### Herança múltipla?!

- Java **não suporta** herança múltipla de classes

  - `extends` só pode ser seguido de um único nome de classe, não de vários

- Mas...  uma classe pode implementar múltiplas interfaces

  - `implements` pode ser seguido de vários nomes de interfaces







## Bibliografia


Robert Sebesta. Conceitos de Linguagens de Programação. Bookman, 2018. Disponível no Portal de E-books da UFSM: http://portal.ufsm.br/biblioteca/leitor/minhaBiblioteca.html (Capítulos 11 e 12)

Java Programming MOOC: 

- Method and Constructor Overloading: https://java-programming.mooc.fi/part-5/2-method-and-constructor-overloading
- Class Inheritance: https://java-programming.mooc.fi/part-9/1-inheritance
- Interfaces: https://java-programming.mooc.fi/part-9/2-interface

