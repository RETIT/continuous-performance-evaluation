FROM postgres:9.6

# Set German locale
RUN localedef -i de_DE -c -f UTF-8 -A /usr/share/locale/locale.alias de_DE.UTF-8

# Create user and database
ENV LANG=de_DE.utf8
ENV POSTGRES_PASSWORD=cargotracker
ENV POSTGRES_USER=cargotracker