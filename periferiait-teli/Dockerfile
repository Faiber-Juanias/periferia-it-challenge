# ---- Build stage (node:22 full: trae toolchain para deps nativas) ----
FROM node:22 AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build

# ---- Runtime stage (slim: solo dependencias de producción) ----
FROM node:22-slim AS runtime
WORKDIR /app
ENV NODE_ENV=production
ENV PORT=4200
COPY package.json package-lock.json ./
RUN npm ci --omit=dev
COPY --from=build /app/dist ./dist
EXPOSE 4200
CMD ["node", "dist/periferiait-teli/server/server.mjs"]
