(ns views.colors
	(:refer-clojure :exclude [second extend])
	(:require [hiccup.core :refer :all]
			  [garden.core :refer [css]]
			  [garden.color :as color :refer [hsl rgb]]))

(def orange ["#AA6C39" "#FFD0AA" "#D49A6A" "#804616" "#552700"])
(def yellow ["#AA8439" "#FFE3AA" "#D4B16A" "#805C16" "#553900"])
(def blue ["#2E4372" "#7887AB" "#4F628E" "#162956" "#061639"])
(def teal ["#226666" "#669999" "#407F7F" "#0D4D4D" "#003333"])
(def red ["#AA3939" "#FFAAAA" "#D46A6A" "#801515" "#550000"])
(def green ["#2D882D" "#88CC88" "#55AA55" "#116611" "#004400"])
(def yellower ["#D4C26A" "#FFF7CC" "#FCEDA5" "#AA9739" "#7D6B13"])
(def purple ["#615192" "#B4AACF" "#8678AD" "#413075" "#261656"])
(def indigo ["#4F618E" "#A8B2CD" "#7584A9" "#2E4272" "#152754"])
(def yellowest ["#FCFCA5" "#FFFFE6" "#FFFFCA" "#D4D46A" "#A7A735"])
(def purple-again ["#9571A8" "#DFD2E7" "#BCA2CA" "#764A8E" "#56286F"])
(def violet ["#8578AD" "#D9D4E8" "#B0A7CD" "#615192" "#3E2E73"])
(def pink ["#9571A8" "#DFD2E6" "#BCA2C9" "#774A8D" "#572870"])


(def new_red (rgb 44 25 25))
(def new_grey (rgb 38 38 38))
(def new_light_grey (rgb 52 52 52))
(def new_yellow (rgb 246 191 40))
(def new_white (rgb 209 209 209))
(def new_bright_red (rgb 170 30 30))


#_(def theme ["#363e59" "#f2f2f2" "#115945" "#bf8773" "#8c513b"])
#_(def theme ["#363e59" "#f2f2f2" "#115945" new_red "#8c513b"])

(def theme {
	:background new_grey
	:field new_light_grey 
	:accent new_bright_red 
	:text-accent new_yellow 
	:text new_white})




