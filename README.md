# WDPRiR - Wstęp do programowania równoległego i rozproszonego
## Lab 1
Polegało na przetestowaniu możliwości (i trudności) w implementacji wielowątkowości

task1() - printuje obecną liczbę wątków 

task2() - uruchamia wątek który printuje hello world 

task3() - sumowanie tablicy sekwencyjne

task4() - sumowanie tablicy równolegle - wersja podstawowa 

task5() - sumowanie tablicy równolegle - używając stałej puli wątków - CountDownLatch 

task6() - sumowanie tablicy równolegle - używając AtomicInteger z metodą getAndSet()

task7() - sumowanie tablicy równolegle - używając AtomicInteger z metodą getAndSet() w sposób zmodyfikowany

Taski 3-7 wypluwały output w postaci plików .csv, z których można stworzyć wykresy w pythonie (patrz plik chart.py)

## Lab 2
Polegało na parsowaniu strony internetowej w celu znalezenia plików .png i przepuszczeniu ich przez filtr 
Gaussa w celu wkurzenia studentów -  w wersji sekwencyjnej i równoleglej (AtomicInteger)
