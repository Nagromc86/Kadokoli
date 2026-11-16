# Kadokoli

Suivi des **colis** et des **cadeaux** (cartes cadeaux, bons d'achat, coffrets, e-cartes, avoirs) dans une appli Android.

- Télécharger l'appli : https://github.com/Nagromc86/kadokoli/releases/latest/download/kadokoli.apk
- L'appli propose elle-même les mises à jour (Mon compte › Mises à jour).

## Organisation

- `src/app.html` : l'appli (une seule page). `python3 tools/build.py` assemble `index.html` avec les polices et librairies de `tools/vendor/`.
- `android/` : emballage Capacitor ; `.github/workflows/apk.yml` construit, signe et publie l'APK à chaque modification.
- Signature : secrets `APK_KEYSTORE` (base64 du .p12, alias `kadokoli`) et `APK_KEYSTORE_PASSWORD`. La clé doit rester la même pour que les mises à jour s'installent par-dessus.
- Les données restent sur le téléphone (sauvegarde exportable en JSON).
