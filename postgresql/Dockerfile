FROM postgres:9.6

# Set German locale
RUN localedef -i de_DE -c -f UTF-8 -A /usr/share/locale/locale.alias de_DE.UTF-8
ENV LANG de_DE.utf8

# Create user and database
ADD init.sql /docker-entrypoint-initdb.d/init.sql