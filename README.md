# grammarly-tech-challenge
Tech challenge by Grammarly proposed during ScalaUA 2017

Given misspelled word find all words from the dictionary, distance from which is less then n and larger then nMin.

Distance is defined as Levenstein edit distance https://en.wikipedia.org/wiki/Levenshtein_distance

# Example
misspelled word - caqe
dictionary - book, books, cake, boo, cape, cart, boon, cook, caqes
result - cart (2 subst), cape (1 subst), cake (1 subst), caqes (1 deletion)
