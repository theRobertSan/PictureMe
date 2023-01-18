package com.example.pictureme.views.addFeeling.adapters

class SelectedPosition {
    companion object{
        private var feelingsPosition: Int = 0
        private var foodPosition: Int = -1
        var currentPosition: Int = -1

        fun changePage(onFoodPage: Boolean) {
            if (onFoodPage) {
                feelingsPosition = currentPosition
                if (feelingsPosition != -1) {
                    foodPosition = -1
                }
                currentPosition = foodPosition
            } else {
                foodPosition = currentPosition
                if (foodPosition != -1) {
                    feelingsPosition = -1
                }
                currentPosition = feelingsPosition
            }
        }

        fun anyChecked(): Boolean {
            return feelingsPosition != -1 && foodPosition != -1
        }
    }
}