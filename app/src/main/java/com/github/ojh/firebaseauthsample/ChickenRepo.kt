package com.github.ojh.firebaseauthsample

/**
 * Created by ohjaehwan on 2017. 2. 15..
 */
data class ChickenRepo (
    val chicken: List<Chicken>
) {
    constructor() : this(listOf())
}