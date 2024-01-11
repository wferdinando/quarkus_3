db.createUser(
    {
        user: "wfit",
        pwd: "wfit",
        roles:[
            {
                role: "readWrite",
                db:"pix"
            }
        ]
    }
);
db.createCollection("transacao_pix");