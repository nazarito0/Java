Студент групи CIc-21 Партика Назар

Завдання:
В програмі показати використання внутрішніх(inner), вкладених(staticnested) та локальних класів. Показати зв'язки між класами: асоціація, композиція, агрегація.
Реалізувати мінімальну взаємодію між екземплярами класів.

14. Водойми.

1.Тему можна обрати з наведених у завданнях до лабораторної роботи №4 або взяти предметну область, яку використовуєте при моделюванні бази даних з курсу "Організація баз даних". А також, можна обрати предметну область за Вашим бажанням.

2. Бажано використати абстрактні класи (abstract class), інтерфейси (interface) й таким чином показати наслідування і реалізацію (implementation). Хто хоче - можна використати sealed class, record.

3. Класи мають містити властивості (поля) і поведінку (методи) для взаємодії між собою.

4. Ознайомтесь з статичними класами і методами у Вашій предметній області. Приклади придумайте в контексті обраної предметної області (завдання).

5. Створені класи також відобразіть за допомогою UML-діаграми. Можна скористатись вбудованими засобами Intelli J Idea або PlantUML, або які маєте бажання.

![alt text](<Screenshot from 2026-03-12 19-50-18.png>)

![alt text](<Screenshot from 2026-03-12 20-03-36.png>)

```@startuml
interface Swimable <<interface>> {
  +swim()
}
abstract class WaterBody <<abstract>> {
  -name : String
  +WaterBody(name : String)
  +describe()
}
class Water <<sealed>> {
  -waterType : String
  +showWaterType()
}
class FreshWater {
  .. no additional fields/methods ..
}
class SaltWater {
  .. no additional fields/methods ..
}
class Lake {
  -water : Water
  +Lake(name : String)
  +describe()
  +createIsland()
}
class Fish {
  -species : String
  +Fish(species : String)
  +swim()
  +fishFacts()
}
class Island {
  -islandName : String
  +Island(name : String)
  +showIslandInfo()
}
WaterBody <|-- Lake
Water <|-- FreshWater
Water <|-- SaltWater
Swimable <|.. Fish
Lake *-- Water
Lake *-- Island
Lake +-- Fish
@enduml```