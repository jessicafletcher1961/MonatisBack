# Formules de plus-value et de rendement

Pour un compte financier sur une periode, on peut poser les variables suivantes :

```text
S0 = solde au debut de periode
S1 = solde a la fin de periode
A  = apports / versements sur la periode
R  = retraits sur la periode
F  = frais payes sur la periode
```

## Formule simple, sans apport ni retrait

```text
Plus-value = S1 - S0

Taux de plus-value = (S1 - S0) / S0 * 100
```

Exemple : 10 000 au debut, 10 800 a la fin :

```text
(10 800 - 10 000) / 10 000 * 100 = 8 %
```

## Avec apports et retraits

Il faut neutraliser les mouvements ajoutes ou retires par l'utilisateur, sinon on confond un versement avec une performance du placement.

```text
Plus-value nette = S1 - S0 - A + R
```

Equivalent :

```text
Plus-value nette = S1 - (S0 + A - R)
```

Dans le service, cela correspond a l'idee suivante :

```text
montantPlusMoinsValueNette =
    soldeFinPeriode
    - (soldeDebutPeriode + montantRecettes - montantDepenses)
```

## Avec frais

Si les frais sont deja sortis du compte, le solde final S1 est deja net de frais.

```text
Plus-value nette apres frais = S1 - S0 - A + R
```

Pour obtenir une performance brute avant frais :

```text
Plus-value brute avant frais = S1 + F - S0 - A + R
```

Autrement dit :

```text
plusValueBrute = plusValueNette + frais
```

Point d'attention : dans le code actuel, la formule nommee "nette" ajoute les frais :

```java
soldeFinPeriode + montantFraisEnCentimes - soldeDebutAvecOperations
```

D'un point de vue financier classique, cette formule ressemble plutot a une plus-value brute avant frais. La formule sans ajout des frais correspond davantage a une plus-value nette apres frais.

## Taux de rendement

Une fois la plus-value calculee, il faut choisir une base de calcul.

Formule simple :

```text
Taux = Plus-value / Capital de reference * 100
```

Pour rester proche du modele actuel :

```text
Capital de reference = S0 + A - R
```

Donc :

```text
Taux net = Plus-value nette / (S0 + A - R) * 100
Taux brut = Plus-value brute / (S0 + A - R) * 100
```

Avec protection :

```text
si Capital de reference = 0, le taux n'est pas calculable
```

## Formule plus juste si les apports et retraits ont des dates

Pour un vrai taux de rendement financier, il vaut mieux ponderer les mouvements selon leur duree d'investissement. C'est la methode de Dietz modifiee :

```text
Gain = S1 - S0 - somme(flux)

Base = S0 + somme(flux_i * poids_i)

Taux = Gain / Base * 100
```

Avec :

```text
flux_i > 0 pour un apport
flux_i < 0 pour un retrait
poids_i = nombre de jours entre le flux et la fin de periode / nombre total de jours
```

Cette methode est plus juste qu'un simple `S0 + A - R`, parce qu'un versement fait la veille de la fin ne doit pas peser autant qu'un versement fait au debut de la periode.

## Recommandation

Pour une application de comptabilite familiale, il est utile de distinguer clairement :

```text
Plus-value nette = S1 - S0 - apports + retraits

Plus-value brute = Plus-value nette + frais

Taux net = Plus-value nette / capitalDeReference * 100

Taux brut = Plus-value brute / capitalDeReference * 100
```

Pour des placements avec versements reguliers, la formule la plus propre est la methode de Dietz modifiee.
