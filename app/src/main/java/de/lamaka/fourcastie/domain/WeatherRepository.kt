package de.lamaka.fourcastie.domain

import de.lamaka.fourcastie.domain.model.Weather

interface WeatherRepository {

    /*
    Планирую чтобы репо просто бросал эксепшены (нет сети, ошибка загрузки и т.д) + маппинг из объектов сетевого слоя на домен,
     а вью модель отлавливала их
        1. добавить имплементацию репозитория
        2. посмотреть про checked-unchecked эксепшены
     */
    suspend fun loadForCity(name: String): Weather
}