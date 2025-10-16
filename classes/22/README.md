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


[![LiaScript](https://raw.githubusercontent.com/LiaScript/LiaScript/master/badges/course.svg)](https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2025b/main/classes/22/README.md)

# Programação Orientada a Objetos (5)




> Este material faz parte de uma introdução ao paradigma de **programação orientada a objetos** em linguagem Java.


## Conceitos da POO


![](img/oo-extends.png)

A programação orientada a objetos se baseia em alguns conceitos (independentes de sintaxe) que favorecem organização, manutenção, compreensão e reuso de código. 

Conceitos básicos:

- [ ] Encapsulamento: https://en.wikipedia.org/wiki/Encapsulation_%28computer_programming%29

- [ ] Herança: https://en.wikipedia.org/wiki/Inheritance_%28object-oriented_programming%29

- [ ] Polimorfismo: https://en.wikipedia.org/wiki/Polymorphism_%28computer_science%29



## Quizzes

As 10 questões abaixo têm correção automática:


                 {{1}}
************************************************

Os atributos e métodos de um objeto representam, respectivamente... 

[( )] seu comportamento e seu estado
[(x)] seu estado e seu comportamento

************************************************

                 {{2}}
************************************************

Em um sistema de e-commerce em Java, há uma classe 'Product' que contém uma String chamada 'sku' para representar o código de um produto e um float 'price' para representar o preço do produto.

Uma variável 'prod' é declarada como sendo da classe 'Product'. Qual das seguintes afirmações está correta?

[( )] sku e price são instâncias de Product
[( )] Product é uma instância de prod
[(x)] prod terá seu próprio valor de price
[( )] prod é um atributo de Product


************************************************

                 {{3}}
************************************************

Qual das opções abaixo é um construtor válido para uma classe 'Person'?


[( )] `public void Person() { }`
[(x)] `public Person(String name) { }`
[( )] `public initPerson() {}`
[( )] `void constructPerson() { }`


************************************************

                 {{4}}
************************************************

Verdadeiro ou falso? "Um método público pode acessar somente atributos públicos declarados na classe a que ele pertence."

[( )] Verdadeiro
[(x)] Falso


************************************************

                 {{5}}
************************************************

As declarações de métodos abaixo são válidas dentro de uma classe Person?

``` java
public void method(int a) { }

public void method(float b) { }
```

[(x)] Sim
[( )] Não


************************************************


                 {{6}}
************************************************

As declarações de métodos abaixo são válidas dentro de uma classe Person?

``` java
public int method(int a) { }

public void method(int a) { }
```

[( )] Sim
[(x)] Não


************************************************

                 {{7}}
************************************************

Considerando o código abaixo, qual das opções é válida para criar uma instância de Person?


``` java
public class Person {
  private String name;
  private int age;
  public Person(String n, int a) {
    name = n;
    age = a;
  }
}
```

[(x)] Person p = new Person("Maria", 18);
[( )] Object p = new Person("Maria", "18");
[( )] Person p = new Person();
[( )] Object p = new Person();
***********************************************************************
Em Java, se não for definido nenhum construtor em uma classe, o compilador automaticamente fornece um construtor padrão sem argumentos. Esse construtor padrão é essencialmente um construtor vazio.
No entanto, se for definido qualquer construtor com parâmetros na classe, o Java não fornecerá mais automaticamente um construtor sem argumentos. Nesse caso, caso ainda seja necessário um construtor sem argumentos, será necessário defini-lo explicitamente.
***********************************************************************

************************************************

                 {{8}}
************************************************

Quantos atributos estão declarados na classe Person? 

``` java
public class Person {
  private String name;
  private int age;
  public Person(String n, int a) {
    name = n;
    age = a;
  }
}
```

[[2]]

************************************************


                 {{9}}
************************************************

O código `Person p;` cria um objeto da classe Person em Java?


[( )] Sim
[(x)] Não




************************************************

                 {{10}}
************************************************

Para descontrair :-) Java é uma linguagem que suporta o paradigma de programação... 

[( )] orientado a dificuldades
[( )] orientado a gambiarras
[(x)] orientado a objetos
[( )] orientado a games

************************************************



## Diagramas de classe (UML)

<div style="display: flex; flex-direction: column; gap: 20px;">
  <div style="display: flex; align-items: center; width: 100%; margin-bottom: 20px;">
    <img src="img/umlthumb.png" alt="Description" style="width: 150px; height: 150px; object-fit: cover; margin-right: 20px; border: 2px solid #ccc; border-radius: 8px;">
    <div style="margin-left: 15px;">
      <blockquote class="lia-quote">Programas orientados a objetos são organizados em muitas classes que <b>se relacionam</b> umas com as outras</blockquote>
      
    </div>
  </div>


  <!-- Add more items here -->
</div>



- Modelagem de software com UML (Unified Modeling Language): intersecção com engenharia de software

  - na Wikipedia: https://en.wikipedia.org/wiki/Unified_Modeling_Language
  - especificação oficial: https://www.omg.org/spec/UML/

- Diagramas de classe descrevem a estrutura de um software graficamente

  - classes representadas por retângulos compartimentados, separando nome da classe, atributos, métodos
  - relações entre classes representadas por linhas/flechas interligando as classes
  - na Wikipedia: https://en.wikipedia.org/wiki/Class_diagram







### Diagramas de aulas anteriores


<h4>Aula passada</h4>

```mermaid @mermaid
classDiagram

  class SvgTemplate {

    - bgColor1 : String
    - bgColor2 : String
    - accent1 : String
    - accent2 : String
    - SvgTemplate(String bg1, String bg2, String accent1, String accent2)
    + fromChoice(String choice) : SvgTemplate$ 
    + render(String title1, String title2, String name, String reason) : String
  }
  class Main {

    + main(String[] args) : void $ 
  }
  class CardGenerator {

    - template : SvgTemplate
    + CardGenerator(SvgTemplate template)
    + generateCard(Achievement a) : String
    + saveCard(Path outputDir, String studentName, String svgContent) : void
  }
  class CsvReader {

    + read(Path csvPath) : List<Achievement>
  }
  class Achievement {

    - name : String
    - title : String
    - reason : String
    + Achievement(String name, String title, String reason)
    + getName() : String
    + getTitle() : String
    + getReason() : String
  }

Main ..> CsvReader : uses
Main ..> Achievement : uses
Main ..> SvgTemplate : uses
Main ..> CardGenerator : uses
CardGenerator ..> SvgTemplate : depends on
CardGenerator ..> Achievement : uses
```

<h4>Aula retrasada</h4>

```mermaid @mermaid
classDiagram
    direction LR

    class Group {
        - String name
        - ArrayList~Student~ students
        - ArrayList~Professor~ professors
        + Group()
        + Group(String name)
        + String getName()
        + void setName(String name)
        + void addMember(Student s)
        + void addMember(Professor p)
        + ArrayList~String~ getContactInfos()
        + boolean userExists(String userId)
        + int countMembers()
    }

    class Student {
        - String name
        - String userId
        - String course
        + Student()
        + Student(String name, String userId, String course)
        + String getName()
        + void setName(String name)
        + String getUserId()
        + void setUserId(String userId)
        + String getCourse()
        + void setCourse(String course)
        + String getContactInfo()
    }

    class Professor {
        - String name
        - String userId
        - String room
        - String building
        + Professor()
        + Professor(String name, String userId, String room, String building)
        + String getName()
        + void setName(String name)
        + String getUserId()
        + void setUserId(String userId)
        + String getRoom()
        + void setRoom(String room)
        + String getBuilding()
        + void setBuilding(String building)
        + String getContactInfo()
    }

    class Main {
        + main(String[] args)
    }

    Group --> "0..*" Student : has-a
    Group --> "0..*" Professor : has-a
    Main ..> Group : uses
    Main ..> Student : uses
    Main ..> Professor : uses
```



### Relações entre classes

Podem ser: dependência, associação, agregação, composição, herança, realização/implementação

- Dependência: uma classe usa objeto de outra classe (mudanças na segunda podem afetar a primeira)
- Associação: atributo de uma classe referencia uma instância (ou instâncias) de outra classe
- Agregação: tipo de associação entre "um todo" e uma parte, que pode existir independentemente
- Composição: tipo de associação entre "um todo" e uma parte, sendo que a parte não pode existir independentemente


Mais sobre isso em: 

- Visual Paradigm: uma ferramenta clássica de modelagem de software
- https://blog.visual-paradigm.com/what-are-the-six-types-of-relationships-in-uml-class-diagrams/#Association_Relationships




```mermaid @mermaid
classDiagram
    direction LR

    class Group {
        - String name
        - ArrayList~Student~ students
        - ArrayList~Professor~ professors
        + Group()
        + Group(String name)
        + String getName()
        + void setName(String name)
        + void addMember(Student s)
        + void addMember(Professor p)
        + ArrayList~String~ getContactInfos()
        + boolean userExists(String userId)
        + int countMembers()
    }

    class Student {
        - String name
        - String userId
        - String course
        + Student()
        + Student(String name, String userId, String course)
        + String getName()
        + void setName(String name)
        + String getUserId()
        + void setUserId(String userId)
        + String getCourse()
        + void setCourse(String course)
        + String getContactInfo()
    }

    class Professor {
        - String name
        - String userId
        - String room
        - String building
        + Professor()
        + Professor(String name, String userId, String room, String building)
        + String getName()
        + void setName(String name)
        + String getUserId()
        + void setUserId(String userId)
        + String getRoom()
        + void setRoom(String room)
        + String getBuilding()
        + void setBuilding(String building)
        + String getContactInfo()
    }

    class Main {
        + main(String[] args)
    }

    Group --> "0..*" Student : has-a
    Group --> "0..*" Professor : has-a
    Main ..> Group : uses
    Main ..> Student : uses
    Main ..> Professor : uses
```



## Herança

- Outro tipo de relação entre classes 
- Inspirada no "mundo real": 

  - pais **TRANSMITEM** aos filhos suas características e comportamento!

- Motivação: evitar **redundâncias** - mais produtividade, mais facilidade de manutenção
- Menos frequente que dependência/associação/agregação em programas "pequenos", mas muito usado em frameworks

![Imagem inspirada no domínio da biologia/genética, com uma árvore genealógica de insetos (joaninhas). No topo da árvore há duas joaninhas (pai-mãe) e abaixo delas há 4 descendentes. As descendentes têm características em comum com suas ascendentes, mas também possuem características próprias (padrões de pintas). A herança genética, exemplificada nesta imagem, serve de inspiração para a herança na programação orientada a objetos.](img/ladybug.png)


### Problema: Student, Professor, Group

Problema:

- Classes Student e Professor têm alguns atributos idênticos (**redundância**): name, userId
- Classe Group tem código redundante: 2 ArrayList, métodos com código **redundante** (mesmo algoritmo aplicado a Student e Professor)

Student.java

``` java
public class Student {
  private String name;
  private String userId;
  private String course;

  // ...
}
```

Professor.java

``` java
public class Professor {
  private String name;
  private String userId;
  private String room;
  private String building;

  // ...
}
```

Group.java

``` java
public class Group {
  private String name;
  private ArrayList<Student> students;
  private ArrayList<Professor> professors;

  // ...

  public void addMember(Student s) {
    this.students.add(s);
  }

  public void addMember(Professor p) {
    this.professors.add(p);
  }
  
  public ArrayList<String> getContactInfos() {
    ArrayList<String> contact = new ArrayList<String>();
    for (Student s: students) {
      contact.add(s.getContactInfo());
    }
    for (Professor p: professors) {
      contact.add(p.getContactInfo());
    }
    return contact;
  }  

}
```

### Solução do problema

Eliminar redundância com herança:

1. Criar uma classe Person com atributos/métodos comuns a Student e Professor
2. Criar Student e Professor como classes derivadas (que herdam atributos/métodos) de Person 
3. Em Group, substituir ArrayList<Student> e ArrayList<Professor> por ArrayList<Person>


### Em Java: `extends`

Nova classe `Person` (super-classe, classe mãe):

- é uma classe como outra qualquer que vimos até agora

``` java
class Person {
  private String name;
  public Person() {
    this.name = "to be given";
  }
  public String getName() {
    return this.name;
  }
}
```


Classe `Student` derivada de `Person`:

- usa `extends` em sua definição para indicar que herda atributos/métodos de outra classe
- outras linguagens de OOP suportam o mesmo conceito mas variam a sintaxe e particularidades

``` java
class Student extends Person {
  private String course;
  public Student() {
    this.course = "to be chosen";
  }
}
```

### Em UML

Diagrama de uma solução para a prática da aula retrasada:

> Agora só temos `ArrayList<Person>` em Group!


```mermaid @mermaid
classDiagram
    direction LR

    class Main {
        +main(String[] args)
    }

    class Person {
        -String name
        -String userId
        +Person()
        +Person(String name, String userId)
        +String getName()
        +void setName(String name)
        +String getUserId()
        +void setUserId(String userId)
        +String getContactInfo()
    }

    class Student {
        -String course
        +Student()
        +Student(String name, String userId, String course)
        +String getCourse()
        +void setCourse(String course)
        +String getContactInfo()
    }

    class Professor {
        -String room
        -String building
        +Professor()
        +Professor(String name, String userId, String room, String building)
        +String getRoom()
        +void setRoom(String room)
        +String getBuilding()
        +void setBuilding(String building)
        +String getContactInfo()
    }

    class Group {
        -String name
        -ArrayList~Person~ members
        +Group()
        +Group(String name)
        +String getName()
        +void setName(String name)
        +void addMember(Person m)
        +ArrayList~String~ getContactInfos()
        +boolean userExists(String userId)
        +int countMembers()
    }

    Main ..> Group : uses
    Main ..> Student : uses
    Main ..> Professor : uses
    Group "0..*" --> Person : has-a
    Person <|-- Student : inherits
    Person <|-- Professor : inherits
```



Anterior:



```mermaid @mermaid
classDiagram
    direction LR

    class Group {
        - String name
        - ArrayList~Student~ students
        - ArrayList~Professor~ professors
        + Group()
        + Group(String name)
        + String getName()
        + void setName(String name)
        + void addMember(Student s)
        + void addMember(Professor p)
        + ArrayList~String~ getContactInfos()
        + boolean userExists(String userId)
        + int countMembers()
    }

    class Student {
        - String name
        - String userId
        - String course
        + Student()
        + Student(String name, String userId, String course)
        + String getName()
        + void setName(String name)
        + String getUserId()
        + void setUserId(String userId)
        + String getCourse()
        + void setCourse(String course)
        + String getContactInfo()
    }

    class Professor {
        - String name
        - String userId
        - String room
        - String building
        + Professor()
        + Professor(String name, String userId, String room, String building)
        + String getName()
        + void setName(String name)
        + String getUserId()
        + void setUserId(String userId)
        + String getRoom()
        + void setRoom(String room)
        + String getBuilding()
        + void setBuilding(String building)
        + String getContactInfo()
    }

    class Main {
        + main(String[] args)
    }

    Group --> "0..*" Student : has-a
    Group --> "0..*" Professor : has-a
    Main ..> Group : uses
    Main ..> Student : uses
    Main ..> Professor : uses
```


### Herança implica em...

> Fim do copy/paste de código entre classes! :-)

- objeto da classe Student terá **atributo herdado** de Person: name 
- objeto da classe Student terá **método herdado** de Person: getName()
- fica implícito que classes usuárias de `Student` podem chamar `getName()`


``` java
class Person {
  private String name;
  public Person() {
    this.name = "to be given";
  }
  public String getName() {
    return this.name;
  }
}

class Student extends Person {
  private String course;
  public Student() {
    this.course = "to be chosen";
  }
}

class Main {
 public static void main(String[] args) {
   Person p = new Person();
   Student s = new Student();
   System.out.println(p.getName());
   System.out.println(s.getName()); // herança em ação!
 }
}
```

### Construtores 

> Construtor de classe-mãe é chamado implicitamente **ANTES** do construtor da filha!


``` java
class Person {
  private String name;
  public Person() {
    System.out.println("Construtor de Person");
  }
  public String getName() {
    return this.name;
  }
}

class Student extends Person {
  private String course;
  public Student() {
    System.out.println("Construtor de Student");
  }
}

class Main {
  public static void main(String[] args) {
    // chama construtor de Person e depois de Student
    Student s = new Student();
  }
}
```

### Encapsulamento

> Atributos e métodos **privados** da classe-mãe **NÃO PODEM** ser acessados na classe-filha

``` java
class Student extends Person {
  private String course;
  public Student() {
    System.out.println("Construtor de Student");
    this.course = "CC";
    this.name = ""; // erro: name is private
  }
}
```

### Visibilidade `protected`

> Torna atributos e métodos visíveis a classes derivadas (mas não a outras classes "fora da família")

``` java
class Person {
  protected String name; // agora é protected!
  public Person() {
    System.out.println("Construtor de Person");
  }
  public String getName() {
    return this.name;
  }
}

class Student extends Person {
  private String course;
  public Student() {
    System.out.println("Construtor de Student");
    this.course = "CC";
    this.name = ""; // OK agora!
  }
}
```

### Herança: relação "is-a"

Herança também implica em: 

- Student "is-a" Person (todo estudante é uma pessoa)
- Logo, objeto Student pode ser usado onde se espera Person
- Contrário não se aplica: nem toda pessoa é estudante

> Objeto da classe derivada pode ser usado onde se manipula super-classe

``` java
class Main {
  public static void main(String[] args) {
    Person p = new Person();
    Student s = new Student();
    ArrayList<Person> lis = new ArrayList<Person>();
    lis.add(p);
    lis.add(s);
  }
}
```

### "is-a" X "has-a"

Lembre-se que classes podem se relacionar de diferentes maneiras

| "is-a" (é-um)   | "has-a" (tem-um)   |
| :--------- | :--------- |
| herança (generalização/especialização)     | agregação/composição     |
| `class Student extends Person { }` | `class Person { String name; }` | 
| Student is a Person | Person has a name | 





### Quiz: quando usar herança?

- Lembre alguns objetivos da OOP: código organizado, mais fácil de ler e modificar
- Herança evita redundâncias: código repetitivo com algumas substituições
- Muitas bibliotecas/frameworks **EXIGEM** que você crie suas classes derivadas de outras existentes
- Quando estiver criando sua hierarquia de classes, aplique o teste "is-a"

Faça o teste:

- [[x]] class Piano extends Instrumento
- [[ ]] class Lista extends Elemento
- [[ ]] class Pessoa extends Administrador
- [[x]] class Cerveja extends Bebida
- [[ ]] class Ferrari extends Motor
- [[ ]] class Bebida extends Vinho
- [[x]] class Prata extends Metal
- [[ ]] class Button extends Window
- [[x]] class Felino extends Animal
- [[ ]] class Vehicle extends Bus


## Herança no "mundo real"!

Avance para ver alguns links que ilustram herança em códigos do "mundo real" 

### Java: `Object`

- Em Java, todas as classes derivam implicitamente da classe `java.lang.Object`.

- Devido a isso, dada uma referência para qualquer objeto, podemos sempre chamar alguns métodos definidos em `Object` (por exemplo: toString(), equals(), hashCode(), getClass(), wait()/notify(), etc.)

- Em outras linguagens

  - C++ é diferente, não tem hierarquia com uma única superclasse
  - C# é semelhante, tem classe Object
  - Python é semelhante, tem classe object

### Em documentação

- Hierarquia de classes do pacote `javax.swing` para criação de interfaces gráficas para desktop: 

  https://docs.oracle.com/en/java/javase/21/docs/api/java.desktop/javax/swing/package-tree.html

- Hierarquia de classes do framework Spring Boot para aplicações web (backend): 

  https://docs.spring.io/spring-boot/docs/current/api/overview-tree.html

O que há em comum?

> Em Java, todas as classes derivam implicitamente da classe `Object` !

### Em games de Paradigmas



| Thumb | Game  | Repo | Exemplo com herança |
| ----- | ----- | ---- | ------------------- |
| ![](img/ArqueoSaga-280.png) | [Arqueo Saga](https://ifspohr.itch.io/arqueo-saga) | [arqueo-saga](https://github.com/elc117/game-2024b-arqueo-saga) | [QuizScreen.java](https://github.com/elc117/game-2024b-arqueo-saga/blob/master/core/src/main/java/io/github/t2paradigmas/QuizScreen.java)                                          |
| ![](img/GuillotineClicker-280.png) | [Guillotine Clicker](https://pinhalgrandense.itch.io/guillotineclicker)     | [t4-2022a-flavio_gregori_matheus](https://github.com/elc117/t4-2022a-flavio_gregori_matheus) | [GuillotineClicker.java](https://github.com/elc117/t4-2022a-flavio_gregori_matheus/blob/main/core/src/com/mygdx/game/GuillotineClicker.java)                                                                                                                                               |
| ![](img/JonasArcanaInvaders-280.png) | [Jonas vs Arcana Invaders](https://guglis.itch.io/jonas-vs-arcana-invaders) | [Jonas-Vs-Arcana-Invaders](https://github.com/elc117/Jonas-Vs-Arcana-Invaders)               | [JonasVsArcanaInvaders.java](https://github.com/elc117/Jonas-Vs-Arcana-Invaders/blob/main/core/src/com/uga/game/JonasVsArcanaInvaders.java)                                                                                                                                                |
| ![](img/UfsmRockstar-280.png) | [UFSM Rock Star](https://henrique-krever.itch.io/ufsm-rockstar)             | [2021gamet4-nos-temos-o-nata](https://github.com/elc117/2021gamet4-nos-temos-o-nata)         | [UfsmRockstar.java](https://github.com/elc117/2021gamet4-nos-temos-o-nata/blob/main/core/src/com/ufsm/rockstar/UfsmRockstar.java)                                                                                                                                                          |
| ![](img/Syrene-280.png) | [Syrene](https://alegz.itch.io/syrene)                                      | [game-Alexandre-ChagasBrites](https://github.com/elc117/game-Alexandre-ChagasBrites)         | [BoxCollider.java](https://github.com/elc117/game-Alexandre-ChagasBrites/blob/master/core/src/com/alegz/mermaid/physics/BoxCollider.java), [CircleCollider.java](https://github.com/elc117/game-Alexandre-ChagasBrites/blob/master/core/src/com/alegz/mermaid/physics/CircleCollider.java) |




Encontre mais jogos em:

- https://itch.io/jam/games-paradigmas-programacao-ufsm-2024b
- https://itch.io/jam/games-paradigmas-programacao-ufsm-2022a
- https://itch.io/jam/games-paradigmas-programacao-ufsm-2021a
- https://itch.io/jam/games-paradigmas-programacao-ufsm-2020a


## Prática


- Parte 1: você vai completar um código fornecido
- Parte 2: cocê vai criar um código do zero

Avance para ver os detalhes...

### Parte 1: Herança em Assignments

Sua empresa está desenvolvendo um software assistente na organização de tarefas (assignments) de disciplinas de faculdade. Uma das funcionalidades do software é a geração de mensagens de notificação sobre cada tarefa. As tarefas podem ser individuais ou em grupo, e tarefas em grupo têm dados adicionais. Você vai colaborar no desenvolvimento de algumas classes para resolver este problema.



``` mermaid @mermaid
classDiagram
    direction LR

    class Assignment {
        -LocalDate dueDate
        -String description
        -boolean pending
        -LocalDate submitDate
        +Assignment(LocalDate dueDate, String description)
        +String getDescription()
        +boolean isPending()
        +void complete(LocalDate date)
        +int daysLeft()
        -String status()
        +String message()
    }

    class GroupAssignment {
        -String teamMates
        +GroupAssignment(LocalDate dueDate, String description, String teamMates)
        +String message()
    }

    class TrackAssignments {
        +main(String[] args)
    }

    TrackAssignments ..> Assignment : uses
    GroupAssignment "1" --|> Assignment : is-a
```



1. Você vai trabalhar com arquivos na pasta `01-assignments`. Os arquivos são os seguintes:

   - [Assignment.java](src/01-assignments/Assignment.java):  classe que representa uma tarefa
   - [GroupAssignment.java](src/01-assignments/GroupAssignment.java): classe que representa uma tarefa a ser desenvolvida em grupo
   - [TrackAssignments](src/01-assignments/TrackAssignments.java): classe que contém o método main, que cria e manipula uma lista de tarefas


2. Compile e execute o código fornecido, usando comandos vistos nas práticas anteriores. Você verá mensagens indicando que o código tem que ser completado.



3. Na classe `Assignment`, implemente o método `public String toString()`, de forma que o primeiro laço em `TrackAssignments` produza o seguinte:

   ```
   ==> Printing all assignment **OBJECTS**:
   { dueDate='2024-11-28', description='game', pending='true', submitDate='null'}
   { dueDate='2024-11-01', description='java01', pending='true', submitDate='null'}
   { dueDate='2024-11-02', description='java02', pending='true', submitDate='null'}
   { dueDate='2024-11-03', description='java03', pending='true', submitDate='null'}
   ```
   Veja mais sobre o método `toString`:

   - em português [aqui](http://www.mauda.com.br/?p=1472) ou 
   - em inglês [aqui](https://runestone.academy/ns/books/published/csawesome/Unit9-Inheritance/topic-9-7-Object.html)

4. Na classe `Assignment`, complete o método `status`  para retornar uma String que represente a situação da tarefa:

   - "done" se a tarefa estiver completa (não pendente)
   - "late" se a tarefa estiver pendente e atrasada
   - "due in x days" se a tarefa estiver pendente, faltando x=daysLeft() dias para a entrega

5. Na classe `GroupAssignment`, note que o construtor usa a palavra-chave `super`. Veja mais sobre isso:

   - Uso de `super` em construtores: https://materialpublic.imd.ufrn.br/curso/disciplina/2/8/8/4
   - Outra forma de usar `super`: https://www.w3schools.com/java/ref_keyword_super.asp
   - Consulte **MESMO** este material acima sobre `super`, pois vai ser útil na questão seguinte!

6. Na classe `GroupAssignment`, complete o método `notificationMessage()` para retornar uma mensagem modificada quando a tarefa for em grupo, conforme o exemplo abaixo:

   ```
   ==> Printing all assignment **MESSAGES**:
   Group Assignment game is due in 18 days - call teamMate1, teamMate2
   Assignment java01 is late
   Group Assignment java02 is late - call teamMate1
   Group Assignment java03 is due in 4 days - call teamMate1
   ```
   Dicas:

   - Identifique um "padrão" nas mensagens de notificação: o que é variável e o que é constante na mensagem modificada?
   - Para evitar redundância, use `super` para aproveitar/reusar a mensagem implementada na superclasse


7. Na classe `TrackAssignments`, no final do método `main`, adicione um código para contar e mostrar a quantidade de tarefas concluídas (não pendentes).


8. Se você completou tudo corretamente, o resultado da execução do código agora terá esta forma (com algumas diferenças dependendo da data em que você executar):

   ```
   ==> Printing all assignment **OBJECTS**:
   { dueDate='2024-11-28', description='game', pending='true', submitDate='null'}
   { dueDate='2024-11-01', description='java01', pending='true', submitDate='null'}
   { dueDate='2024-11-02', description='java02', pending='true', submitDate='null'}
   { dueDate='2024-11-14', description='java03', pending='true', submitDate='null'}

   ==> Printing all assignment **MESSAGES**:
   Group Assignment game is due in 18 days - call teamMate1, teamMate2
   Assignment java01 is late
   Group Assignment java02 is late - call teamMate1
   Group Assignment java03 is due in 4 days - call teamMate1

   ==> Printing all assignment messages **AGAIN**:
   Assignment game is done
   Assignment java01 is late
   Group Assignment java02 is late - call teamMate1
   Group Assignment java03 is due in 4 days - call teamMate1

   ==> Completed assignments: 1

   ```





### Parte 2: Herança em Quizzes



Nesta parte, você vai criar um programa "do zero", escrevendo todo o código. O programa deverá ter uma hierarquia de classes representando diferentes tipos de questões de quizzes, uma classe representando um quiz e um programa principal que irá criar e fazer algumas operações com um quiz.

1. Crie uma superclasse `Question`, com atributos/métodos comuns a qualquer tipo de quiz (você tem liberdade para defini-los - não há uma única forma correta de representar isso).

2. Crie pelo menos 2 classes derivadas de `Question`, representando outros tipos de questões, por exemplo: verdadeiro/falso, múltipla-escolha, preencher lacunas, etc. Estas classes especializadas deverão ter atributos específicos.

3. Crie uma classe `Quiz`, que deverá armazenar e gerenciar uma lista de questões, como no exemplo com herança na classe `Group` da aula anterior. 

4. Crie uma classe `Main`, que deverá criar um quiz com várias questões de diferentes classes. Depois de criá-lo, você deverá fazer pelo menos 2 operações à sua escolha  (por exemplo, mostrar as questões, verificar resposta de uma questão, sortear uma questão, etc.), de acordo com os métodos que você implementou na classe `Quiz`.








## Bibliografia


Robert Sebesta. Conceitos de Linguagens de Programação. Bookman, 2018. Disponível no Portal de E-books da UFSM: http://portal.ufsm.br/biblioteca/leitor/minhaBiblioteca.html (Capítulos 11 e 12)
