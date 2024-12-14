package tasks;

import common.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {
  /*
   * поле count удаляем
   */

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  public List<String> getNames(List<Person> persons) {
    /*
     * Стримы умеют корректно работать с пустыми коллекциями, поэтому в проверке нет смысла.
     * Вместо удаления первого объекта мы просто пропускаем его.
     */
    return persons.stream()
        .skip(1)
        .map(Person::firstName)
        .collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    /*
     * Используем вышеописанный метод (getNames) для получения списка имен.
     * Передаём его в конструктор HashSet.
     * Останутся только уникальные значения.
     */
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
    /*
     * Создаем стрим из Ф, И, О.
     * Фильтруем на !null и склеиваем.
     */
    return Stream.of(person.secondName(), person.firstName(), person.middleName())
        .filter(Objects::nonNull)
        .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    /*
     * Почему-то была создана мапа с капасити 1 (хотя их чаще будет больше одного)
     * Заменили на стримы
     * Так как не описано, что делать, если несколько объектов имеют одинаковый id - берем первый
     */
    return persons.stream()
        .collect(Collectors.toMap(Person::id, this::convertPersonToString, (p1, p2) -> p1));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    /*
     * Код лаконичнее
     */
    return persons1.stream()
        .anyMatch(persons2::contains);
    
    /*
     * Есть ещё один вариант, возможно он быстрее
     * Set<Person> person1Set = new HashSet<>(persons1);
     * return persons2.stream().anyMatch(person1Set::contains);
     */
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    /*
     * Вместо использования переменной, используем count().
     * Переменную удаляем.
     */
    return numbers
        .filter(num -> num % 2 == 0)
        .count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    /*
     * В сете могут быть только уникальные объекты. Хешкод у Integer - само число.
     * Так как все наши объекты уникальны (и уникальны их хешкоды), то каждый 
     * объект будет в отдельном бакете. При вычислении бакета для объекта получится 
     * так, что номер бакета будет равен хешкоду, то есть самому числу. Так как
     * бакеты хранятс в массиве, выводится они будут по порядку, а их порядок
     * соответствует значению, которое они хранят. Получается "отсортированный Set".
     */
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
}
