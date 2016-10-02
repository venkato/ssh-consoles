@set GROOVY_OPTS=%GROOVY_OPTS% -Djrrclasspath=classpath.groovy 

@set GROOVY_OPTS=%GROOVY_OPTS% -DjrrcasspathAddToSystemClassLoader=false

gr.bat src\com\jpto\sample\SampleLauncher.groovy launch 
