
import asyncio
from agrobr import datasets


async def main():
    df = await datasets.preco_diario("soja")

    if df.empty:
        print("Nenhuma cotação foi encontrada.")
        return

    print("\nCOTAÇÕES REAIS DE SOJA")
    print("=" * 70)

    print(
        df[
            ["data", "produto", "praca", "valor", "unidade", "fonte"]
        ].to_string(index=False)
    )

    arquivo = "cotacoes_soja_reais.csv"

    df.to_csv(
        arquivo,
        index=False,
        encoding="utf-8"
    )

    print(f"\nCSV exportado: {arquivo}")
    print(f"Total de registros: {len(df)}")


if __name__ == "__main__":
    asyncio.run(main())
