.\gradlew shadowJar
javadoc -private -splitindex -author -version -d %docupage% --ignore-source-errors -sourcepath.\src\main\java\zork\*
cd %docupage%
git add .
git commit -am "Webpage update through script"
git push origin master
