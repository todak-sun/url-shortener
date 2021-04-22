FROM node:12-alpine as builder
WORKDIR /usr/local/app
COPY package.json ./

RUN npm install
COPY ./ ./
RUN npm run build

FROM nginx
EXPOSE 80
COPY --from=builder /usr/local/app/build /usr/share/nginx/html