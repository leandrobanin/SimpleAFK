### 💤 SimpleAFK – Plugin de AFK para Minecraft
SimpleAFK é um plugin leve e direto que gerencia jogadores AFK no servidor. Ideal para servidores que desejam identificar e lidar com jogadores inativos sem configurações complicadas ou dependências externas.

### 🔧 Funcionalidades
- Detecta automaticamente quando um jogador está AFK (sem se mover por X minutos).
- Comando /afk para que o jogador se marque manualmente como AFK.
- Exibição de status AFK no tab ou chat (opcional).
- Permite configurar o tempo limite de inatividade para entrar ou sair do modo AFK.
- Cancelamento automático do modo AFK ao se mover, falar ou interagir.

### 📦 Instalação
1. Baixe o arquivo .jar do plugin.
2. Coloque-o na pasta plugins/ do seu servidor Spigot ou Paper.
3. Reinicie ou recarregue o servidor.


### ⚙️ Configuração
```
# ---------------------------------- #
#    Configuração do SimpleAFK       #
# ---------------------------------- #

# Tempo em SEGUNDOS para um jogador ser considerado AFK por inatividade.
afk-timer-seconds: 300

# --- Mensagens Customizáveis (Enviadas apenas para o jogador) ---
# Use MiniMessage para cores! Ex: <green>, <bold>, ou cores hexadecimais <#FF5733>.
messages:
  # Mensagens para AFK por inatividade
  private-afk-notification: "<gray>Você agora está AFK por inatividade."
  private-no-longer-afk: "<green>Você não está mais AFK!"

  # Mensagens para o comando /afk manual
  manual-set-afk: "<gray>Você entrou no modo AFK."
  manual-unset-afk: "<green>Você saiu do modo AFK."
```

### 📜 Comandos

/safk - informações do plugin;
/safk reload - reinicia as configurações do plugin;
/afk - ative ou desative o AFK;

