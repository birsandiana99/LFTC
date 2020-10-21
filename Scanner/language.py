operators = ["adauga_cai_putere", "scade_cai_putere", "inmulteste_cai_putere",
             "imparte_cai_putere", "este", "nu este", "mai_multi_cai_putere_decat",
             "mai_putini_cai_putere_decat", "mai_multi_sau_egali_cai_putere_decat",
             "mai_putini_sau_egali_cai_putere_decat", "&&", "||", "%", "/"]


separators = [",","go","space","{","}","-", " "]

reserved_words =["bagi_intaia","bagi_char","incepe_cursa","continua_cat_timp","pune_pe_afis","pornesti_motorul",
                 "gata_cursa","daca_motor","altfel"]


language = operators + separators + reserved_words
codify = {}
codify['identifier'] = 0
codify['constant'] = 1

for i in range (2,len(language)+2):
    codify[language[i-2]] = i


print (codify)