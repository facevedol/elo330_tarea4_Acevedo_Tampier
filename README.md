elo330_tarea4_Acevedo_Tampier
=============================

ELO330 Tarea 4: Emunador de Variaciones de Retardo y Pérdidas en Transferencias UDP en Java

Compilación: 
============
$ make

Ejecución:
==========
$ java ERP retardo_promedio(milisecs) variación_retardo(milisecs) porcentaje_pérdida(0-1) puerto_local [host_remoto] puerto_remoto

Descripción: Este programa funciona como intermediario entre un cliente y un servidor. Su función es simular un retraso en los paquetes UDP como también las posibles perdidas.

Implementación: La implementación del programa se basa en 4 hebras. - Envío desde Cliente a erp_udp - Envío desde erp_udp al Servidor - Envío desde el Servidor a erp_udp - Envío desde erp_udp al Cliente. Además para el paso de paquetes entre hilos se utilizan 2 delayQueue.