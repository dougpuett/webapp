(ns webapp.ff_home
	(:require [hiccup.core :refer :all]
		[views.html_helpers :refer :all]))

(def ff (str site_header (html [:body [:h2 "Championship Odds:"]
	[:div {:class "main"} [:h3 "Before Week 1:"][:p "What's Fappening 42.2%, Team Freedman 29.3%, Bill and Ted's ExcellentAdv 15.5%, Champ's Smelly Meat 13.0%"]]
	[:div {:class "main"} [:h3 "After Week 1:"][:p "Team Freedman 49.4%, Champ's Smelly Meat 39.8%, Bill and Ted's ExcellentAdv 5.7%, What's Fappening 5.2%"]]
	[:h2 "Individual Matches:"]
	[:div {:class "main"} [:h3 "Before Week 1:"][:p "Team Freedman 60.1%, Bill and Ted's ExcellentAdv 39.9%"][:p "What's Fappening 68.6%, Champ's Smelly Meat 31.4%"][:p "Samuel Beckittns 69.6%, Pat Sajak Off 30.4%"][:p "Team buckley 52.5%, I Didn't Know You Were A Cop 47.5%"][:p "Nebraska Cornholers 53.3%, RGIII's iCloud Pics 46.7%"][:p "The Manifest Destinies 82.0%, Team Stanley 18.0%"]]
	[:div {:class "main"} [:h3 "After Week 1:"][:p "Team Freedman 87.0%, Bill and Ted's ExcellentAdv 13.0%"][:p "Champ's Smelly Meat 90.2%, What's Fappening 9.8%"][:p "Samuel Beckittns 60.2%, Pat Sajak Off 39.8%"][:p "Team buckley 90.2%, I Didn't Know You Were A Cop 9.8%"][:p "Nebraska Cornholers 100.0%"][:p "The Manifest Destinies 95.3%, Team Stanley 4.7%"]]])))