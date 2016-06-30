(ns snakegame.handlers
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [snakegame.functions :refer [rand-free-position]]
            [re-frame.core :refer [register-handler register-sub subscribe dispatch dispatch-sync]]))

(def board [35 25])

(def snake {:direction [1 0]
            :body [[3 2] [2 2] [1 2] [0 2]]})

(def initial-state {:board board
                    :snake snake
                    :point (rand-free-position snake board)
                    :points 0
                    :game-running? true})

(register-handler                  
 :initialize                       
 (fn
   [db _]                         
   (merge db initial-state)))    

(register-sub
  :board
  (fn
    [db _]
    (reaction (:board @db))))  

